/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author sandr
 */
public class Aeropuerto {
    private ArrayList<String> aviones = new ArrayList<>();
    private ArrayList<String> buses = new ArrayList<>();
    private Lock cerrojo = new ReentrantLock();
    
    
    public void añadirBus(String id){
        cerrojo.lock();
        try{
            buses.add(id);
            System.out.print("BUSES"+buses.toString()+"\n");
        }finally{
            cerrojo.unlock();
        }
        
    }
    public void añadirAvion(String id){
        cerrojo.lock();
        try{
            aviones.add(id);
            System.out.print("AVIONES"+aviones.toString()+"\n");
        }finally{
            cerrojo.unlock();
        }
    }

    public ArrayList<String> getAviones() {
        return aviones;
    }

    public ArrayList<String> getBuses() {
        return buses;
    }

    @Override
    public String toString() {
        return "Aeropuerto{" + "aviones=" + aviones + ", buses=" + buses + '}';
    }
    
    
}
