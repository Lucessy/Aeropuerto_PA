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
public class Bus extends Thread{
    private int capacidad;
    private String id, stringNumId;
    private Random random = new Random();
    private AtomicInteger capacidadAeropuerto;
    
    public void run(){
        while(true){
            int miliseg = 5000+random.nextInt(5000);
            llegadaCiudad();
            try{
                Thread.sleep(miliseg);
            }catch (InterruptedException e) {
                System.err.println("El sleep fue interrumpido");
            }
            llegadaAeropuerto(this.capacidad);
            if (capacidadAeropuerto==0){
                
            }
            
        }
    }
    
    //Constructor
    public Bus(int id){
        this.capacidad=0;
        this.id = generarId(id);
    }
    
    //Gnerar el id del bus con formato B-XXXX 
    public String generarId(int num){
        stringNumId = String.format("%04d", num+1);
        id = "B-"+stringNumId;
        System.out.print(id+"\n");
        return id;
    }
    
    //Recoger de 0-50 pasajeros durante 2-5 segundos
    public void llegadaCiudad(){
        int miliseg = 2000+random.nextInt(3000);
        this.capacidad = random.nextInt(50);
        try{
            Thread.sleep(miliseg);
        }catch (InterruptedException e) {
            System.err.println("El sleep fue interrumpido");
        }
    }
    
    public void llegadaAeropuerto(int pasajeros){
        for(int i=0;i<pasajeros;i++){
            capacidadAeropuerto.incrementAndGet();
        }
    }
}
