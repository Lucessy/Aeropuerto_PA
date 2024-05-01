/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 *
 * @author sandr
 */
public class Aeropuerto {

    private Log log;

    private BlockingQueue aviones = new LinkedBlockingQueue();
    private BlockingQueue buses = new LinkedBlockingQueue();
//    private PuertaEmbarque puertaBarcelona = new PuertaEmbarque();
//    private PuertaEmbarque puertaMadrid = new PuertaEmbarque();    

    private BlockingQueue aeroviaIda = new LinkedBlockingQueue();
    private BlockingQueue aeroviaVuelta = new LinkedBlockingQueue();
    
    private ArrayList<Avion> listaPista = new ArrayList<>();

    //private BlockingQueue hangar = new LinkedBlockingQueue();
    private AtomicInteger pasajerosAeropuerto;
    private final String nombre;
    private int posPista = 0;

    private final boolean[] pistas = new boolean[4];
    private final boolean[] puertasEmbarque = new boolean[6]; //Puerta 1 -> Desembarque, 2,3,4,5 -> Otros, 6 -> Embarque
    private final Semaphore semPista = new Semaphore(1);
    private final Semaphore semDisponibilidadPista = new Semaphore(4, true);
    private final Semaphore semEmbarque = new Semaphore(1);
    
    private ReentrantLock lockPista;

    private Semaphore semDisponibilidadEmb = new Semaphore(0, true);
    private Semaphore semDisponibilidadDesemb = new Semaphore(0, true);
    
    private Semaphore taller = new Semaphore (20, true);
    private Lock puertaTaller = new ReentrantLock();
    
    private Random random = new Random();

    private ReentrantLock lockLista;
       
    // Constructor
    public Aeropuerto(String nombre, Log log) {
        this.nombre = nombre;
        this.log = log;
        this.pasajerosAeropuerto = new AtomicInteger(0);
        for (int i = 0; i < 4; i++) {
            pistas[i] = false;
        }
    }

    // Métodos
    public void addBus(Bus bus) { //Pasar objeto Bus
        try {
            buses.put(bus);
            log.escribirArchivo("Bus " + bus.getIdBus() + " es creado.", nombre);

        } catch (InterruptedException ex) {
            log.escribirArchivo("Error en la inserción del bus: " + bus.getIdBus(), nombre);

        }

    }

    public void addAvion(Avion avion) { //Pasar objeto Avion 
        try {
            aviones.put(avion);

            log.escribirArchivo("Avion " + avion.getIdAvion() + " es creado.", nombre);

        } catch (InterruptedException ex) {
            log.escribirArchivo("Error en la inserción del avión: " + avion.getIdAvion(), nombre);
        }
    }

    /*ZONAS DE ACTIVIDAD*/
    public void hangar(Avion avion) {
        Central.dormir(15000, 30000);
//        try{
//            hangar.put(avion);
//        }catch (InterruptedException ex) {
//            System.out.println("Error en la inserción del avión en el hangar");
//        }
    }

    public void taller(Avion avion) throws InterruptedException {
        try{
            taller.acquire();
            puertaTaller.lock();//Por la puerta solo pasa un avion y tarda 1 segundo en hacer la accion
            Central.dormir(1000,1000);
            puertaTaller.unlock();
            if (avion.getNumVuelos()==15){
                Central.dormir(5000, 10000);
                avion.setNumVuelos(0);//Al llegar a 15 vuelos se reinicia el contador
            }else{
                Central.dormir(1000, 5000);
            }
            puertaTaller.lock();
            avion.sleep(1000);
            puertaTaller.unlock();
            taller.release();
        }catch (InterruptedException ex) {
            System.out.println(ex);
       }
        
    }

    public void areaEstacionamiento(Avion avion) throws InterruptedException {
        lockLista.lock();
        try {
             
            listaPista.add(avion); // Lista FIFO que representa la llegada real de los hilos
            int numPasajeros = avion.getNumPasajeros(); // Deberiamos poner un booleano que diga si esEmbarque
            
            if (numPasajeros == 0) {    //El avion quiere embarcar porque tiene 0 pasajeros
                semDisponibilidadEmb.acquire();
                puertasEmbarque(numPasajeros);

            } else {  //El avion tiene capacidad >0 por lo que contiene pasajeros que desembarcar
                semDisponibilidadDesemb.acquire();
                puertasEmbarque(numPasajeros);

            }
        } finally {
            lockLista.unlock();
        }
    }

    public void areaEstacionamientoDesembarque(Avion avion) throws InterruptedException{
        try{
            int tiempo = 1000+ random.nextInt(4000); //Tiempo entre 1-5s de comprobaciones después de desembarcar
            avion.sleep(tiempo);
        }catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
//        } finally {
//            lockLista.unlock();
//        }
    

    public void puertasEmbarque(int numPasajeros) throws InterruptedException {
        semEmbarque.acquire();
        
            for (int i = 0; i < 5; i++) {
                if (puertasEmbarque[i] == false) {
                    puertasEmbarque[i] = true;
                }
            }
        // Intenta embarcar el número máximo de pasajeros
        boolean maxPasajeros = false;
        int intentos = 0;
        while(!maxPasajeros && intentos<3){
            
        }
        
    }
    
    

//    public void areaEstacionamientoDesembarque(Avion avion) throws InterruptedException{
//        try{
//            int tiempo = 1000+ random.nextInt(4000); //Tiempo entre 1-5s de comprobaciones después de desembarcar
//            avion.sleep(tiempo);
//        }catch (InterruptedException ex) {
//            System.out.println(ex);
//        }
//    }
////        } finally {
////            lockLista.unlock();
////        }
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

    public int areaRodaje() throws InterruptedException {
        semDisponibilidadPista.acquire();
        lockPista.lock();

        int posPista = getPista();

        lockPista.lock();

        return posPista;
    }

    public int getPista() {

        for (int i = 0; i < 4; i++) {
            if (pistas[i] == false) {
                pistas[i] = true;
                posPista = i;
            }
        }

        return posPista;
    }
    
    public void accederAerovia(){
        
    }
    
    public int solicitarPista(){
        while(!lockPista.tryLock()){
            Central.dormir(1000, 5000);
        }
        
        int posPista = getPista();
        
        lockPista.unlock();
        
        return posPista;
    }

    public void liberarPista(int posPista) throws InterruptedException {
//        if (!flag boton){
//            pistas[posPista]=false;
//        }
        pistas[posPista] = false;
        semDisponibilidadPista.release();
    }

    public synchronized void aeroviaIda(Avion avion) {
        aeroviaIda.add(avion);
    }

//    public synchronized void aeroviaVuelta(Avion avion) {
//        int indexAvion = aeroviaVuelta.indexOf(avion);
//        aeroviaVuelta.remove(indexAvion);
//    }

    /*FIN ZONAS DE ACTIVIDAD*/
    public BlockingQueue getAviones() {
        return aviones;
    }

    public BlockingQueue getBuses() {
        return buses;
    }

    public AtomicInteger getPasajerosAeropuerto() {
        return pasajerosAeropuerto;
    }

    public String getNombre() {
        return nombre;
    }

}
