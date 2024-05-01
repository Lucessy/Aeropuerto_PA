package aeropuertos;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Aeropuerto {

    // ATRIBUTOS AEROPUERTO
    private AtomicInteger pasajerosAeropuerto;
    private String nombre;
    private int posPista = 0;
    private Lock lockPasajeros = new ReentrantLock();

    // CLASE LOG
    private Log log;

    //  LISTA DE AVIONES EN AEROVIAS
    private Queue aeroviaIda = new ConcurrentLinkedQueue();
    private Queue aeroviaVuelta = new ConcurrentLinkedQueue();

    //  LISTA DE AVIONES EN ZONAS COMPARTIDAS PARA ACTUALIZAR MENU
    private Queue hangar = new ConcurrentLinkedQueue();
    private Queue estacionamiento = new ConcurrentLinkedQueue();
    private Queue taller = new ConcurrentLinkedQueue();

    // PUERTAS DE EMBARQUE
//    private PuertaEmbarque puertaBarcelona = new PuertaEmbarque();
//    private PuertaEmbarque puertaMadrid = new PuertaEmbarque();
    private ArrayBlockingQueue<Integer> indicesPuertas = new ArrayBlockingQueue<>(4);
    private boolean[] puertasEmbarque = new boolean[6]; //Puerta 1 -> Desembarque, 2,3,4,5 -> Otros, 6 -> Embarque
    private Semaphore semDisponibilidadPuertas = new Semaphore(6, true);
    private Semaphore semEmbarque = new Semaphore(1);//Exclusivo para la puerta de embarque
    private Semaphore semDesembarque = new Semaphore(1);//Exclusivo para la puerta de desembarque
    private ReentrantLock lockPuertas = new ReentrantLock(true);
//    private Condition embarcar = lockPuertas.newCondition();
//    private Condition desembarcar = lockPuertas.newCondition();
//    private Semaphore semDisponibilidadEmb = new Semaphore(0, true);
//    private Semaphore semDisponibilidadDesemb = new Semaphore(0, true);

    // PISTAS
    private boolean[] pistas = new boolean[4];
    private Semaphore semPista = new Semaphore(1);
    private Semaphore semDisponibilidadPista = new Semaphore(4, true);
    private Lock lockPista = new ReentrantLock();

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
        hangar.offer(avion.getIdAvion());

        if (reposar) {
            Central.dormir(15000, 30000);
        }
    }

    /**
     * Entra en la zona compartida TALLER y se añade a la lista concurrente.
     *
     * @param avion
     * @throws InterruptedException
     */
    public void taller(Avion avion) throws InterruptedException {
        try {
            semTaller.acquire();

            semPuertaTaller.acquire();//Por la puerta solo pasa un avion y tarda 1 segundo en hacer la accion
            Central.dormir(1000, 1000);
            semPuertaTaller.acquire();

            taller.offer(avion.getIdAvion());

            if (avion.getNumVuelos() == 15) {
                Central.dormir(5000, 10000);
                avion.setNumVuelos(0);//Al llegar a 15 vuelos se reinicia el contador
            } else {
                Central.dormir(1000, 5000);
            }

            semPuertaTaller.acquire();
            Central.dormir(1000, 1000);
            semPuertaTaller.acquire();

            taller.remove(avion.getIdAvion());

            semTaller.release();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }

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
//
    /**
     *
     * @param avion
     * @throws InterruptedException
     */
    public void areaEstacionamiento(Avion avion) throws InterruptedException {
        hangar.remove(avion.getIdAvion()); //Sale el avión del hangar y entra en el estacionamiento

        estacionamiento.offer(avion.getIdAvion());

    }

    /**
     *
     * @param avion
     * @throws InterruptedException
     */
    public void areaEstacionamientoDesembarque(Avion avion) throws InterruptedException {
        try {
            int tiempo = 1000 + random.nextInt(4000); //Tiempo entre 1-5s de comprobaciones después de desembarcar
            avion.sleep(tiempo);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
//        } finally {
//            lockLista.unlock();
//        }
//    }

//    public int puertasEmbarque(boolean esEmbarque) throws InterruptedException {
//        int indice=-1;
//        if (esEmbarque){
//            if(puertasEmbarque[0]=false){
//                semEmbarque.acquire();//Cuando embarquen los pasajeros se usara para ponerlo a false
//                puertasEmbarque[0]=true;
//            }else{
//                semPuertasCompartidas.acquire();
//                indice = indicesPuertas.take();//Puede obtener indices del 1-4
//                puertasEmbarque[indice]=true;
//            }
//        }else{
//            if(puertasEmbarque[5]=false){
//                semDesembarque.acquire();
//            }else{
//                semPuertasCompartidas.acquire();
//                indice = indicesPuertas.take();//Puede obtener indices del 1-4
//                puertasEmbarque[indice]=true;
//            }
//        }
//        
//    }
//    public void puertasEmbarque(int numPasajeros) throws InterruptedException {
//        semEmbarque.acquire();
//        for (int i = 0; i < 5; i++) {
//            if (puertasEmbarque[i] == false) {
//                puertasEmbarque[i] = true;
//            }
//        }
//    }
//     public void puertasDesembarque(int numPasajeros) throws InterruptedException {
//        semEmbarque.acquire();
//        for (int i = 1; i < 6; i++) {
//            if (puertasEmbarque[i] == false) {
//                puertasEmbarque[i] = true;
//            }
//        }
//    }
    /**
     *
     * @param avion
     * @throws InterruptedException
     */
    public void puertasEmbarque(Avion avion) throws InterruptedException {
        semEmbarque.acquire();

//        for (int i = 0; i < 5; i++) {
//            if (puertasEmbarque[i] == false) {
//                puertasEmbarque[i] = true;
//            }
//        }
        puertasEmbarque[0] = true;

        // Intenta embarcar el número máximo de pasajeros
        boolean maxPasajeros = false;
        int intentos = 0;
        int capacidad = avion.getCapacidad();
        int pasajeros = 0;

        while (!maxPasajeros && intentos < 3) {
            pasajeros = getPasajerosDisponibles(capacidad);
            if (pasajeros < capacidad) {
                intentos++;
                capacidad = capacidad - pasajeros;
            } else {
                maxPasajeros = true;
            }
        }

        semEmbarque.release();
    }

    /**
     *
     * @param numPasajeros
     * @throws InterruptedException
     */
    public void puertasDesembarque(int numPasajeros) throws InterruptedException {
        semEmbarque.acquire();
        for (int i = 1; i < 6; i++) {
            if (puertasEmbarque[i] == false) {
                puertasEmbarque[i] = true;
            }
        }
    }

    /**
     *
     * @return @throws InterruptedException
     */
    public int areaRodaje() throws InterruptedException {
        semDisponibilidadPista.acquire();
        lockPista.lock();

        int posPista = getPista();

        lockPista.lock();

        return posPista;
    }

    /**
     *
     * @return
     */
    public int getPista() {

        for (int i = 0; i < 4; i++) {
            if (pistas[i] == false) {
                pistas[i] = true;
                posPista = i;
            }
        }

        return posPista;
    }

    /**
     *
     * @param avion
     */
    public void accederAerovia(Avion avion) {
        aeroviaIda.offer(avion);
    }

    /**
     *
     * @return
     */
    public int solicitarPista() {
        while (!lockPista.tryLock()) {
            Central.dormir(1000, 5000);
        }

        int posPista = getPista();

        lockPista.unlock();

        return posPista;
    }

    /**
     *
     * @param posPista
     * @throws InterruptedException
     */
    public void liberarPista(int posPista) throws InterruptedException {
//        if (!flag boton){
//            pistas[posPista]=false;
//        }
        pistas[posPista] = false;
        semDisponibilidadPista.release();
    }

    /**
     *
     * @param avion
     */
    public synchronized void aeroviaIda(Avion avion) {
        aeroviaIda.add(avion);
    }

//    public synchronized void aeroviaVuelta(Avion avion) {
//        int indexAvion = aeroviaVuelta.indexOf(avion);
//        aeroviaVuelta.remove(indexAvion);
//    }

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
                pasajerosAeropuerto.addAndGet(-pasajeros);
                //  Decrementa la cantidad de pasajeros total -(pasajeros)
                Central.actualizarPasajerosAeropuerto(pasajerosAeropuerto.get(), this);
            }
        } finally {
            lockPasajeros.unlock();
        }
        return pasajeros;
    }

    public AtomicInteger getPasajerosAeropuerto() {
        return pasajerosAeropuerto;
    }

    public String getNombre() {
        return nombre;
    }

}
