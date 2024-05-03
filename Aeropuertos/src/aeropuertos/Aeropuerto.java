package aeropuertos;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aeropuerto {

    // ATRIBUTOS AEROPUERTO
    private AtomicInteger pasajerosAeropuerto;
    private String nombre;
    private Lock lockPasajeros = new ReentrantLock();

    // CLASE LOG
    private Log log;

    //  LISTA DE AVIONES EN AEROVIAS
    private Queue aerovia = new ConcurrentLinkedQueue();

    //  LISTA DE AVIONES EN ZONAS COMPARTIDAS PARA ACTUALIZAR MENU
    private Queue<Avion> hangar = new ConcurrentLinkedQueue<>();
    private Queue<Avion> estacionamiento = new ConcurrentLinkedQueue<>();
    private Queue<Avion> taller = new ConcurrentLinkedQueue<>();
    private Queue<Avion> rodaje = new ConcurrentLinkedQueue<>();

    // PUERTAS DE EMBARQUE
    private ArrayBlockingQueue<Integer> indicesPuertas = new ArrayBlockingQueue<>(4);
    private boolean[] puertasEmbarque = new boolean[6]; //Puerta 1 -> Desembarque, 2,3,4,5 -> Otros, 6 -> Embarque
    private Semaphore semDisponibilidadPuertas = new Semaphore(6, true);
    private Lock lockEmbarque = new ReentrantLock(true);//Exclusivo para la puerta de embarque
    private Lock lockDesembarque = new ReentrantLock(true);//Exclusivo para la puerta de desembarque
    private Condition embarcar = lockEmbarque.newCondition();
    private Condition desembarcar = lockDesembarque.newCondition();

    private Queue listaGeneralPista = new ConcurrentLinkedQueue();
//    private Semaphore semDisponibilidadEmb = new Semaphore(0, true);
//    private Semaphore semDisponibilidadDesemb = new Semaphore(0, true);

    // PISTAS
    private boolean[] pistas = new boolean[4];
    private Semaphore semPista = new Semaphore(1);
    private Semaphore semDisponibilidadPista = new Semaphore(4, true);
    private Lock lockPista = new ReentrantLock(true);
    private boolean[] listaBotonPista = new boolean[4];

    // TALLER
    private Semaphore semTaller = new Semaphore(20, true);
    private Semaphore semPuertaTaller = new Semaphore(1, true);

    private Semaphore semPuertasCompartidas = new Semaphore(1);
    private String[] avionesPuertasEmbarque = new String[6];
    private Random random = new Random();
    private Lock lockLista = new ReentrantLock();

    // Constructor
    public Aeropuerto(String nombre, Log log) {
        this.nombre = nombre;
        this.log = log;
        this.pasajerosAeropuerto = new AtomicInteger(0);

        for (int i = 0; i < 4; i++) {
            pistas[i] = false;
            listaBotonPista[i] = false;
        }

        for (int i = 1; i < 5; i++) {
            // Se inicia con un valor por defecto
            indicesPuertas.add(i);
        }

        for (int i = 0; i < 6; i++) {
            avionesPuertasEmbarque[i] = "";
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
        Central.actualizarAviones(avion, true, "textoHangar", hangar, nombre);

        if (reposar) {
            Central.actualizarAviones(avion, false, "textoTaller", taller, nombre);
            Central.dormir(15000, 30000);
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
            Central.actualizarAviones(avion, false, "textoHangar", hangar, nombre);

        } else if (taller.contains(avion)) {
            //Sale el avión del taller y entra en el estacionamiento
            Central.actualizarAviones(avion, false, "textoTaller", taller, nombre);
        }

        Central.actualizarAviones(avion, true, "textoEstacionamiento", estacionamiento, nombre);
    }

    /**
     * Entra en la zona compartida PUERTAS DE EMBARQUE y se añade a la lista
     * concurrente
     *
     * @param avion
     */
    public void puertasEmbarque(Avion avion) {
        try {
            int indice;
            if (puertasEmbarque[0] == true && indicesPuertas.isEmpty()) {
                embarcar.await();
            }
            lockEmbarque.lock();
            //Primero busca ocupar la puerta exclusiva de embarque
            if (puertasEmbarque[0] == false) {
                puertasEmbarque[0] = true;
                
                lockEmbarque.unlock();
                
                indice = 0;
                avion.setPosPuerta(indice);

                Central.actualizarAviones(avion, false, "textoEstacionamiento", estacionamiento, nombre);
                Central.actualizarAvionesSolitario("textoPuerta" + (indice + 1), avion.getIdAvion(), nombre);

            } else if (!indicesPuertas.isEmpty()) {
                semPuertasCompartidas.acquire();
                
                indice = indicesPuertas.poll();//Indices entre 1-4
                puertasEmbarque[indice] = true;
                
                lockEmbarque.unlock();
                semPuertasCompartidas.release();
                
                avion.setPosPuerta(indice);

                Central.actualizarAviones(avion, false, "textoEstacionamiento", estacionamiento, nombre);
                Central.actualizarAvionesSolitario("textoPuerta" + (indice + 1), avion.getIdAvion(), nombre);
            }
        } catch (InterruptedException ex) {
        }
    }
    
    /**
     * 
     * @param avion 
     */
    public void puertasDesembarque(Avion avion) {
        try {
            int indice = -1;
            if (puertasEmbarque[5] == true && indicesPuertas.isEmpty()) {
                desembarcar.await();
            }
            lockDesembarque.lock();
            //Primero busca ocupar la puerta exclusiva de desembarque
            if (puertasEmbarque[5] == false) {
                puertasEmbarque[5] = true;

                lockDesembarque.unlock();

                indice = 5;
                avion.setPosPuerta(indice);
                System.out.println("El avion con id " + avion.getIdAvion() + " ha cogido el lcok de desembarque y se encuentra en la puerta" + avion.getPosPuerta());

                Central.actualizarAviones(avion, false, "textoRodaje", rodaje, nombre);
                Central.actualizarAvionesSolitario("textoPuerta" + (indice + 1), avion.getIdAvion(), nombre);

            } else if (!indicesPuertas.isEmpty()) {
                semPuertasCompartidas.acquire();

                indice = indicesPuertas.poll();//Indices entre 1-4
                puertasEmbarque[indice] = true;

                lockDesembarque.unlock();
                semPuertasCompartidas.release();

                avion.setPosPuerta(indice);
                System.out.println("El avion con id " + avion.getIdAvion() + " ha cogido el lcok de desembarque y se encuentra en la puerta" + avion.getPosPuerta());

                Central.actualizarAviones(avion, false, "textoRodaje", rodaje, nombre);
                Central.actualizarAvionesSolitario("textoPuerta" + (indice + 1), avion.getIdAvion(), nombre);
            }

        } catch (InterruptedException ex) {
        }
    }
    
    /**
     * 
     * @param avion 
     */
    public void salirPuertasEmbarque(Avion avion) {
        Central.actualizarAvionesSolitario("textoPuerta" + (avion.getPosPuerta() + 1), "", nombre);
        lockEmbarque.lock();
        try {
            if (avion.getPosPuerta() == 0) {
                puertasEmbarque[avion.getPosPuerta()] = false;
                embarcar.signal();  //Despierta en orden FIFO al hilo que esta durmiendo
                
            } else {
                //No debería haber riesgo de que algun avión modifique el elemento de ese índice pq el índice no está metido en la lista de piertas disponibles
                puertasEmbarque[avion.getPosPuerta()] = false;
                indicesPuertas.put(avion.getPosPuerta());
                embarcar.signal();  //Despierta en orden FIFO al hilo que esta durmiendo
            }
        } catch (InterruptedException ex) {
        } finally {
            lockEmbarque.unlock();
        }

    }
    
    /**
     * 
     * @param avion 
     */
    public void salirPuertasDesembarque(Avion avion) {
        Central.actualizarAvionesSolitario("textoPuerta" + (avion.getPosPuerta() + 1), "", nombre);
        lockDesembarque.lock();
        try {
            if (avion.getPosPuerta() == 5) {
                puertasEmbarque[avion.getPosPuerta()] = false;
                desembarcar.signal(); //Despierta en orden FIFO al hilo que esta durmiendo
            } else {
                puertasEmbarque[avion.getPosPuerta()] = false;
                indicesPuertas.put(avion.getPosPuerta());
                desembarcar.signal();   //Despierta en orden FIFO al hilo que esta durmiendo
            }
        } catch (InterruptedException ex) {
        } finally {
            lockDesembarque.unlock();
        }
    }

    /**
     *
     * @param avion
     */
    public void areaRodaje(Avion avion) {
        Central.actualizarAviones(avion, true, "textoRodaje", rodaje, nombre);
    }

    /**
     *
     * @param avion
     */
    public void pista(Avion avion) {
        try {
            semDisponibilidadPista.acquire();
            lockPista.lock();

            int posPista = 0;
            for (int i = 0; i < 4; i++) {
                if (pistas[i] == false) {
                    pistas[i] = true;
                    posPista = i;
                    break;
                }
            }

            avion.setPosicionPista(posPista);

            Central.actualizarAviones(avion, false, "textoRodaje", rodaje, nombre);

            Central.actualizarAvionesSolitario("textoPista" + (posPista + 1), avion.getIdAvion(), nombre);

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
        try {
            semDisponibilidadPista.acquire(); //NO SE SI DEBERIA ESTAR ESTO XD

            while (!lockPista.tryLock()) {
                Central.dormir(1000, 5000);
            }

            Central.actualizarAviones(avion, false, "textoAeroM", aeropuertoAntiguo.getAerovia(), aeropuertoAntiguo.getNombre());

            int posPista = 0;
            for (int i = 0; i < 4; i++) {
                if (pistas[i] == false) {
                    pistas[i] = true;
                    posPista = i;
                    break;
                }
            }

            avion.setPosicionPista(posPista);

            Central.actualizarAvionesSolitario("textoPista" + (posPista + 1), avion.getIdAvion(), nombre);

        } catch (InterruptedException ex) {
        } finally {
            lockPista.unlock();
        }
    }

    /**
     *
     * @param avion
     */
    public void liberarPista(Avion avion) {
        if (!listaBotonPista[avion.getPosicionPista()]) {
            pistas[avion.getPosicionPista()] = false;
        }
        Central.actualizarAvionesSolitario("textoPista" + (avion.getPosicionPista() + 1), "", nombre);
        pistas[avion.getPosicionPista()] = false;
        semDisponibilidadPista.release();
    }

    /**
     * Entra en la zona compartida TALLER y se añade a la lista concurrente.
     *
     * @param avion
     */
    public void taller(Avion avion) {
        try {
            semTaller.acquire();

            Central.actualizarAviones(avion, false, "textoEstacionamiento", estacionamiento, nombre);

            //Por la puerta solo pasa un avion y tarda 1 segundo en hacer la accion
            semPuertaTaller.acquire();
            Central.dormir(1000, 1000);
            semPuertaTaller.release();

            Central.actualizarAviones(avion, true, "textoTaller", taller, nombre);

            if (avion.getNumVuelos() == 15) {
                Central.dormir(5000, 10000);
                avion.setNumVuelos(0);//Al llegar a 15 vuelos se reinicia el contador
            } else {
                Central.dormir(1000, 5000);
            }

            semPuertaTaller.acquire();
            Central.dormir(1000, 1000);
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
        Central.actualizarAviones(avion, true, "textoAeroM", aerovia, nombre);
    }

    /*FIN ZONAS DE ACTIVIDAD*/
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
                Central.actualizarPasajerosAeropuerto(0, this);

                //  Coge los pasajeros que se han indicado
            } else {
                pasajeros = numPasajerosMax;
                pasajerosAeropuerto.addAndGet(pasajeros);
                //  Decrementa la cantidad de pasajeros total -(pasajeros)
                Central.actualizarPasajerosAeropuerto(pasajerosAeropuerto.get(), this);
            }
        } finally {
            lockPasajeros.unlock();
        }
        return pasajeros;
    }

//    public void areaEstacionamiento(Avion avion) throws InterruptedException {
//        if (avion.isEmbarcar()){ //Si el avión va a embarcar solicita puerta de embarque
//            lockLista.lock();//Exclusión mutua para acceder a los aviones en pista
//            try {
//                listaPista.add(avion); // Lista FIFO que representa la llegada real de los hilos
//                indice = puertasEmbarque(avion.isEmbarcar());
//                //return indice con la puerta de embarque libre
//            } finally {
//                lockLista.unlock();
//            }
//        
//        } else {  //El avion ha desembarcado y solo tiene que hacer comprobaciones
//                 try{
//                    int tiempo = 1000+ random.nextInt(4000); //Tiempo entre 1-5s de comprobaciones después de desembarcar
//                    avion.sleep(tiempo);
//                }catch (InterruptedException ex) {
//                    System.out.println(ex);
//                }
//    public synchronized void aeroviaVuelta(Avion avion) {
//        int indexAvion = aeroviaVuelta.indexOf(avion);
//        aeroviaVuelta.remove(indexAvion);
//    }
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

}
