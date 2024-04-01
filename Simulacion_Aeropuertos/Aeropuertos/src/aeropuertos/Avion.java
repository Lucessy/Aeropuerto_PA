/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author sandr
 */
public class Avion extends Thread{
    private int capacidad;
    private String id;
    private AtomicInteger capacidadAeropuerto;
    private Aeropuerto aeropuerto;

    //Constructor
    public Avion(String id, Aeropuerto aeropuerto){
        this.capacidad=0;
        this.id=id;
        this.aeropuerto = aeropuerto;
    }
    
    /*public void run(){
        
    }*/
    

    @Override
    public String toString() {
        return "Avion{" + "capacidad=" + capacidad + ", id=" + id + '}';
    }
    
    
}
