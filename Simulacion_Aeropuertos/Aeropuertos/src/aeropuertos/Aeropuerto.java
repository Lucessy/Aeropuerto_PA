/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author sandr
 */
public class Aeropuerto {
    private BlockingQueue aviones = new LinkedBlockingQueue();
    private BlockingQueue buses = new LinkedBlockingQueue();
    
    
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

    @Override
    public String toString() {
        return "Aeropuerto{" + "aviones=" + aviones + ", buses=" + buses + '}';
    }
    
    
}
