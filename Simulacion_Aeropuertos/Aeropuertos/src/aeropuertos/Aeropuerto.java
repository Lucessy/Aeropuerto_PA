/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author sandr
 */
public class Aeropuerto {

    private BlockingQueue aviones = new LinkedBlockingQueue();
    private BlockingQueue buses = new LinkedBlockingQueue();
    private AtomicInteger pasajerosAeropuerto;
    private String nombre;
    
    private boolean[] pistas = new boolean[4];
    private Lock lockPista = new ReentrantLock();

    // Constructor
    public Aeropuerto(String nombre) {
        this.nombre = nombre;
        this.pasajerosAeropuerto = new AtomicInteger(0);
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
    public void hangar(){
        
    }
    
    public void taller(){
        
    }
    
    public void puertasEmbarque(){
        
    }
    
    public void getPista(){
        int posPista = -1;
        
        
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

}
