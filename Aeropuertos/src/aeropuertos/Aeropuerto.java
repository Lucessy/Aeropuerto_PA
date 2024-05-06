package aeropuertos;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Aeropuerto {

    // ATRIBUTOS AEROPUERTO
    private AtomicInteger pasajerosAeropuerto;
    private String nombre;
    private Lock lockPasajeros = new ReentrantLock();

    // CLASE LOG
    private Log log;

    //  LISTA DE AVIONES EN AEROVIAS
    private Queue<Avion> aerovia = new ConcurrentLinkedQueue();

    //  LISTA DE AVIONES EN ZONAS COMPARTIDAS PARA ACTUALIZAR MENU
    private Queue<Avion> hangar = new ConcurrentLinkedQueue<>();
    private Queue<Avion> estacionamiento = new ConcurrentLinkedQueue<>();
    private Queue<Avion> taller = new ConcurrentLinkedQueue<>();
    private Queue<Avion> rodaje = new ConcurrentLinkedQueue<>();

    // PUERTAS DE EMBARQUE
    private ArrayBlockingQueue<Integer> indicesPuertas = new ArrayBlockingQueue<>(4);
    private boolean[] puertasEmbarque = new boolean[6]; //Puerta 1 -> Desembarque, 2,3,4,5 -> Otros, 6 -> Embarque
    private Lock lockEmbarque = new ReentrantLock(true);//Exclusivo para la puertas de embarques
    private Condition embarcar = lockEmbarque.newCondition();
    private Condition desembarcar = lockEmbarque.newCondition();
    private Queue colaPuertaEmbarque = new ConcurrentLinkedQueue<>();

    // PISTAS
    private boolean[] pistas = new boolean[4];
    private Semaphore semDisponibilidadPista = new Semaphore(4, true);
    private Lock lockPista = new ReentrantLock(true);
    private Boolean[] listaBotonPista = new Boolean[4];
    private Boolean[] listaBotonPistaAnterior = new Boolean[4];

    // TALLER
    private Semaphore semTaller = new Semaphore(20, true);
    private Semaphore semPuertaTaller = new Semaphore(1, true);

    // Constructor
    public Aeropuerto(String nombre, Log log) {
        this.nombre = nombre;
        this.log = log;
        this.pasajerosAeropuerto = new AtomicInteger(0);

        for (int i = 0; i < 4; i++) {
            pistas[i] = false;
            listaBotonPista[i] = true;
            listaBotonPistaAnterior[i] = true;
        }

        for (int i = 1; i < 5; i++) {
            // Se inicia con un valor por defecto
            indicesPuertas.add(i);
        }

        for (int i = 0; i < 6; i++) {
            puertasEmbarque[i] = false;
        }
    }

    /*ZONAS DE ACTIVIDAD*/
    
    /**
     * Entra en la zona compartida HANGAR y se añade a la lista concurrente.
     *
     * @param avion
     * @param reposar
     */
    public void hangar(Avion avion, boolean reposar) {
        Servidor.actualizarAviones(avion, true, "textoHangar", hangar, nombre);

        if (reposar) {
            Servidor.actualizarAviones(avion, false, "textoTaller", taller, nombre);
            Servidor.dormir(15000, 30000);
        }
    }

    /**
     * Entra en la zona compartida AREA DE ESTACIONAMIENTO y se añade a la lista
     * concurrente.
     *
     * @param avion
     */
    public void areaEstacionamiento(Avion avion) {
        if (hangar.contains(avion)) {
            //Sale el avión del hangar y entra en el estacionamiento
            Servidor.actualizarAviones(avion, false, "textoHangar", hangar, nombre);

        } else if (taller.contains(avion)) {
            //Sale el avión del taller y entra en el estacionamiento
            Servidor.actualizarAviones(avion, false, "textoTaller", taller, nombre);
        }

        Servidor.actualizarAviones(avion, true, "textoEstacionamiento", estacionamiento, nombre);
    }

    /**
     * Entra en la zona compartida PUERTAS DE EMBARQUE y se añade a la lista
     * concurrente
     *
     * @param avion
     */
    public void puertasEmbarque(Avion avion) {
        try {
            int indice = 0;
            lockEmbarque.lock();
            while (puertasEmbarque[0] == true && indicesPuertas.isEmpty()) {
                colaPuertaEmbarque.offer("Embarcar");
                embarcar.await();
            }

            //Primero busca ocupar la puerta exclusiva de embarque
            if (puertasEmbarque[0] == false) {
                puertasEmbarque[0] = true;

                indice = 0;
                avion.setPosPuerta(indice);

            } else if (!indicesPuertas.isEmpty()) {

                indice = indicesPuertas.poll();//Indices entre 1-4
                puertasEmbarque[indice] = true;

                avion.setPosPuerta(indice);
            }

            Servidor.actualizarAviones(avion, false, "textoEstacionamiento", estacionamiento, nombre);
            Servidor.actualizarAvionesSolitario("textoPuerta" + (indice + 1), avion.getIdAvion(), nombre);

        } catch (InterruptedException ex) {
        } finally {
            lockEmbarque.unlock();
        }
    }

    /**
     *
     * @param avion
     */
    public void puertasDesembarque(Avion avion) {
        try {
            int indice = 0;

            lockEmbarque.lock();

            while (puertasEmbarque[5] == true && indicesPuertas.isEmpty()) {
                colaPuertaEmbarque.offer("Desembarcar");
                desembarcar.await();
            }

            //Primero busca ocupar la puerta exclusiva de desembarque
            if (puertasEmbarque[5] == false) {
                puertasEmbarque[5] = true;

                indice = 5;
                avion.setPosPuerta(indice);

            } else if (!indicesPuertas.isEmpty()) {

                indice = indicesPuertas.poll();//Indices entre 1-4
                puertasEmbarque[indice] = true;

                avion.setPosPuerta(indice);
            }
            //  Agrega los pasajeros del avión al sistema
            pasajerosAeropuerto.addAndGet(avion.getNumPasajeros());

            Servidor.actualizarAviones(avion, false, "textoRodaje", rodaje, nombre);
            Servidor.actualizarAvionesSolitario("textoPuerta" + (indice + 1), avion.getIdAvion(), nombre);

        } catch (InterruptedException ex) {
        } finally {
            lockEmbarque.unlock();
        }
    }

    /**
     *
     * @param avion
     */
    public void salirPuertasEmbarque(Avion avion) {
        lockEmbarque.lock();
        try {
            switch (avion.getPosPuerta()) {
                case 0 -> {
                    puertasEmbarque[avion.getPosPuerta()] = false;
                    embarcar.signalAll();
                    colaPuertaEmbarque.remove("Embarcar");
                }
                case 5 -> {
                    puertasEmbarque[avion.getPosPuerta()] = false;
                    desembarcar.signalAll();
                    colaPuertaEmbarque.remove("Desembarcar");
                }
                default -> {
                    puertasEmbarque[avion.getPosPuerta()] = false;
                    indicesPuertas.put(avion.getPosPuerta());
                    if (colaPuertaEmbarque.poll() == "Embarcar") {
                        embarcar.signalAll();
                    } else {
                        desembarcar.signalAll();
                    }
                }
            }

            Servidor.actualizarAvionesSolitario("textoPuerta" + (avion.getPosPuerta() + 1), "", nombre);

        } catch (InterruptedException ex) {
        } finally {
            lockEmbarque.unlock();
        }
    }

    /**
     *
     * @param avion
     */
    public void areaRodaje(Avion avion) {
        Servidor.actualizarAviones(avion, true, "textoRodaje", rodaje, nombre);
    }

    /**
     *
     * @param avion
     */
    public void pista(Avion avion) {
        try {
            semDisponibilidadPista.acquire();
            lockPista.lock();

            int posPista = -1;
            for (int i = 0; i < 4; i++) {
                if (pistas[i] == false) {
                    pistas[i] = true;
                    posPista = i;
                    break;
                }
            }
            
            avion.setPosicionPista(posPista);

            Servidor.actualizarAviones(avion, false, "textoRodaje", rodaje, nombre);

            Servidor.actualizarAvionesSolitario("textoPista" + (posPista + 1), avion.getIdAvion(), nombre);

        } catch (InterruptedException ex) {
        } finally {
            lockPista.unlock();
        }
    }

    /**
     *
     * @param aeropuertoAntiguo
     * @param avion
     */
    public void solicitarPista(Aeropuerto aeropuertoAntiguo, Avion avion) {
        while(semDisponibilidadPista.tryAcquire()){
            Servidor.dormir(1000, 5000);
        }
        
        lockPista.lock();
        
        try {
            Servidor.actualizarAviones(avion, false, "textoAeroM", aeropuertoAntiguo.getAerovia(), aeropuertoAntiguo.getNombre());
            int posPista = 0;
            for (int i = 0; i < 4; i++) {
                if (pistas[i] == false) {
                    pistas[i] = true;
                    posPista = i;
                    break;
                }
            }
            avion.setPosicionPista(posPista);
            Servidor.actualizarAvionesSolitario("textoPista" + (posPista + 1), avion.getIdAvion(), nombre);
        } finally {
            lockPista.unlock();
        }
    }

    /**
     *
     * @param avion
     */
    public void liberarPista(Avion avion) {
        lockPista.lock();
        try {
            if (listaBotonPista[avion.getPosicionPista()]) {
                pistas[avion.getPosicionPista()] = false;
                semDisponibilidadPista.release();
            }
            Servidor.actualizarAvionesSolitario("textoPista" + (avion.getPosicionPista() + 1), "", nombre);
        } finally {
            lockPista.unlock();
        }
    }

    /**
     * Entra en la zona compartida TALLER y se añade a la lista concurrente.
     *
     * @param avion
     */
    public void taller(Avion avion) {
        try {
            semTaller.acquire();

            //Por la puerta solo pasa un avion y tarda 1 segundo en hacer la accion
            semPuertaTaller.acquire();
            Servidor.dormir(1000, 1000);
            semPuertaTaller.release();

            Servidor.actualizarAviones(avion, false, "textoEstacionamiento", estacionamiento, nombre);

            Servidor.actualizarAviones(avion, true, "textoTaller", taller, nombre);

            if (avion.getNumVuelos() == 15) {
                Servidor.dormir(5000, 10000);
                avion.setNumVuelos(0);//Al llegar a 15 vuelos se reinicia el contador
            } else {
                Servidor.dormir(1000, 5000);
            }

            semPuertaTaller.acquire();
            Servidor.dormir(1000, 1000);
            semPuertaTaller.release();

            semTaller.release();
        } catch (InterruptedException ex) {
        }
    }

    /**
     *
     * @param avion
     */
    public void accederAerovia(Avion avion) {
        Servidor.actualizarAviones(avion, true, "textoAeroM", aerovia, nombre);
    }

    /*FIN ZONAS DE ACTIVIDAD*/
    /**
     * Obtiene el número máximo de pasajeros que puede coger del aeropuerto dada
     * la variable numPasajerosMax
     *
     * @param numPasajerosMax
     * @return
     */
    public int getPasajerosDisponibles(int numPasajerosMax) {
        lockPasajeros.lock();
        int pasajeros = 0;
        try {
            //  No se sube ningún pasajero, porque no hay en el aeropuerto
            if (pasajerosAeropuerto.get() == 0) {
                pasajeros = 0;

                //  Se suben todos los disponibles, que son menos de los que se piden y se iguala a 0
            } else if (pasajerosAeropuerto.get() <= numPasajerosMax) {
                pasajeros = pasajerosAeropuerto.getAndSet(0);
                Servidor.actualizarPasajerosAeropuerto(0, this);

                //  Coge los pasajeros que se han indicado
            } else {
                pasajeros = numPasajerosMax;
                //  Decrementa la cantidad de pasajeros total -(pasajeros)
                pasajerosAeropuerto.addAndGet(-pasajeros);
                Servidor.actualizarPasajerosAeropuerto(pasajerosAeropuerto.get(), this);
            }
        } finally {
            lockPasajeros.unlock();
        }
        return pasajeros;
    }
    
    /**
     * 
     */
    public void cerrarAbrirPistas() {
        lockPista.lock();
        try {
            for (int i = 0; i < 4; i++) {
                // Primer caso, se quiere bloquear la pista y no se está usando la pista
                if (listaBotonPistaAnterior[i] == true && listaBotonPista[i] == false && pistas[i] == false) {
                    semDisponibilidadPista.acquire();
                    pistas[i] = true;
                }
                // Segundo caso, se quiere abrir la pista bloqueada que antes estaba bloqueada
                else if (listaBotonPistaAnterior[i] == false && listaBotonPista[i] == true){
                    pistas[i] = false;
                    semDisponibilidadPista.release();
                }
            }
        } catch (InterruptedException ex) {
        } finally {
            lockPista.unlock();
        }
    }

    //  Get y Set
    public AtomicInteger getPasajerosAeropuerto() {
        return pasajerosAeropuerto;
    }

    public String getNombre() {
        return nombre;
    }

    public Queue getAerovia() {
        return aerovia;
    }

    public void setAerovia(Queue aerovia) {
        this.aerovia = aerovia;
    }

    public Queue<Avion> getHangar() {
        return hangar;
    }

    public void setHangar(Queue<Avion> hangar) {
        this.hangar = hangar;
    }

    public Queue<Avion> getEstacionamiento() {
        return estacionamiento;
    }

    public void setEstacionamiento(Queue<Avion> estacionamiento) {
        this.estacionamiento = estacionamiento;
    }

    public Queue<Avion> getTaller() {
        return taller;
    }

    public void setTaller(Queue<Avion> taller) {
        this.taller = taller;
    }

    public Queue<Avion> getRodaje() {
        return rodaje;
    }

    public void setRodaje(Queue<Avion> rodaje) {
        this.rodaje = rodaje;
    }

    public Boolean[] getListaBotonPista() {
        return listaBotonPista;
    }

    public void setListaBotonPista(Boolean[] listaBotonPista) {
        listaBotonPistaAnterior = this.listaBotonPista;
        this.listaBotonPista = listaBotonPista;
        cerrarAbrirPistas();
    }

}
