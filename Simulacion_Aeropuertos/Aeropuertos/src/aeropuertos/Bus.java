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
            llegadaCiudad();
            conducir();
            llegadaAeropuerto(this.capacidad);
            vueltaAeropuerto();
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
    
    //Esperar 5-10s
    public void conducir(){
        int miliseg = 5000+random.nextInt(5000);
        try{
            Thread.sleep(miliseg);
        }catch (InterruptedException e) {
            System.err.println("El sleep fue interrumpido");
        }
    }
    
    //Aumento de la variable atómica del aeropuerto con los pasajeros que llegan
    public void llegadaAeropuerto(int pasajeros){
        for(int i=0;i<pasajeros;i++){
            capacidadAeropuerto.incrementAndGet();
        }
        //Falta que se suban los pasajeros al autobus
    }
    
   //Se esperan de 2-5 segundos y se cogen los pasajeros que se piden o los que haya disponibles si son menos.
    public void vueltaAeropuerto(){
        int pasajeros = random.nextInt(50);
        int miliseg = 2000+random.nextInt(3000);
        try{
            Thread.sleep(miliseg);
        }catch (InterruptedException e) {
            System.err.println("El sleep fue interrumpido");
        }
        if (capacidadAeropuerto.get() == 0){                
            this.capacidad = 0;                             //No se sube ningún pasajero, porque no hay en el aeropuerto
        }else if (capacidadAeropuerto.get()<=pasajeros){    
            this.capacidad=capacidadAeropuerto.get();       //Se suben todos los disponibles, que son menos de los que se piden
            capacidadAeropuerto.set(0);              
        }else{
            this.capacidad=pasajeros;                       //Coge los pasajeros que se han idicado
            capacidadAeropuerto.set(capacidadAeropuerto.get()-pasajeros);
        }
    }
}
