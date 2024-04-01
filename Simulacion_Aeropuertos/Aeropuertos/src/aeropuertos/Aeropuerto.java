/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author sandr
 */
public class Aeropuerto {
    private BlockingQueue aviones = new LinkedBlockingQueue();
    private BlockingQueue buses = new LinkedBlockingQueue();
    private AtomicInteger pasajerosAeropuerto = new AtomicInteger(0);
    private String nombre;

    public Aeropuerto(String nombre, int pasajeros) {
        this.nombre = nombre;
        this.pasajerosAeropuerto = new AtomicInteger(pasajeros);
    }
    
    public void añadirBus(String id){
        try{
            buses.put(id);
            System.out.print("BUSES"+buses.toString()+"\n");
        } catch (InterruptedException ex) {
            System.out.print("Error en la inserción del bus");
        }
        
    }
    public void añadirAvion(String id){
        try{
            aviones.put(id);
            System.out.print("AVIONES"+aviones.toString()+"\n");
        } catch (InterruptedException ex) {
            System.out.print("Error en la inserción del avión");
        }
    }

    public BlockingQueue getAviones() {
        return aviones;
    }

    public BlockingQueue getBuses() {
        return buses;
    }

    public int getCapacidadAeropuerto() {
        return pasajerosAeropuerto.get();
    }

    public int addCapacidadAeropuerto(int num) {
        return pasajerosAeropuerto.addAndGet(num);
    }
    
    public int restCapacidadAeropuerto(int num) {
        return pasajerosAeropuerto.addAndGet(-(num));
    }

    public AtomicInteger getPasajerosAeropuerto() {
        return pasajerosAeropuerto;
    }
    
    @Override
    public String toString() {
        return "Aeropuerto{" + "aviones=" + aviones + ", buses=" + buses + '}';
    }
    
    
}
