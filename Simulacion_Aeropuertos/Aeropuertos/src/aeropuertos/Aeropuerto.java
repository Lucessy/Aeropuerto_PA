/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


/**
 *
 * @author sandr
 */
public class Aeropuerto {

    private BlockingQueue aviones = new LinkedBlockingQueue();
    private BlockingQueue buses = new LinkedBlockingQueue();
    //private BlockingQueue hangar = new LinkedBlockingQueue();
    private AtomicInteger pasajerosAeropuerto;
    private String nombre;
    private int posPista=0;
    
    private boolean[] pistas = new boolean[4];
    private boolean[] puertasEmbarque = new boolean[6]; //La puerta 1 reservada solo para embarque, la puerta 6 para desembarque, el resto para ambas accioens
    private Semaphore semPista = new Semaphore(1);
    private Semaphore semDisponibilidadPista = new Semaphore(4,true);
    private Semaphore semEmbarque = new Semaphore (1);
    private Semaphore semDisponibilidadEmb = new Semaphore (6, true);

    // Constructor
    public Aeropuerto(String nombre) {
        this.nombre = nombre;
        this.pasajerosAeropuerto = new AtomicInteger(0);
        for(int i=0;i<4;i++){
            pistas[i]=false;
        }
    }

    // Métodos
    public void addBus(Bus bus) { //Pasar objeto Bus
        try {
            buses.put(bus);

            System.out.println("Bus " + bus.getIdBus() + " es creado.");

        } catch (InterruptedException ex) {
            System.out.println("Error en la inserción del bus");
            
        }

    }

    public void addAvion(Avion avion) { //Pasar objeto Avion 
        try {
            aviones.put(avion);

            System.out.println("Avion " + avion.getIdAvion() + " es creado.");

        } catch (InterruptedException ex) {
            System.out.println("Error en la inserción del avión");
        }
    }
    
    /*ZONAS DE ACTIVIDAD*/
    public void hangar(Avion avion){
//        try{
//            hangar.put(avion);
//        }catch (InterruptedException ex) {
//            System.out.println("Error en la inserción del avión en el hangar");
//        }
    }
    
    public void taller(){
        
    }
    
    public void puertasEmbarque(int capacidad) throws InterruptedException{
        semDisponibilidadEmb.acquire();
        semEmbarque.acquire();
        if (capacidad == 0){    //El avion quiere embarcar porque tiene 0 pasajeros
            for (int i = 0;i<4;i++){
                if (puertasEmbarque[i]==false){
                    puertasEmbarque[i]=true;
                }
            }
        }else{  //El avion tiene capacidad >0 por lo que contiene pasajeros que desembarcar
            for (int i=1;i<5;i++){  
                if (puertasEmbarque[i]==false){
                    puertasEmbarque[i]=true;
                }
            }
        }
    }
    
    public int getPista() throws InterruptedException{
        semDisponibilidadPista.acquire();
        semPista.acquire();
        for (int i=0;i<4;i++){
            if (pistas[i]==false){
                pistas[i]=true;
                posPista=i;
            }
        }
        semPista.release();
        return posPista;        
    }
    
    public void liberarPista(int posPista) throws InterruptedException{
//        if (!flag boton){
//            pistas[posPista]=false;
//        }
        pistas[posPista]=false;
        semDisponibilidadPista.release();       
    }
    
    public void areaEstacionamiento(){
        
    }
    
    public void areaRodaje(){
        
    }
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

    private void release() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
