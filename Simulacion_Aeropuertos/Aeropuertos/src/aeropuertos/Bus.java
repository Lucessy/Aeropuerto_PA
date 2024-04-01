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
    private String id;
    private Random random = new Random();
    private AtomicInteger pasajerosAeropuerto;
    
    //Constructor
    public Bus(String id, AtomicInteger pasajerosAeropuerto){
        this.capacidad=0;
        this.id = id;
        this.pasajerosAeropuerto = pasajerosAeropuerto;
    }
    
    public void run(){
        while(true){
            llegadaCiudad();
            conducir();
            llegadaAeropuerto(this.capacidad);
            vueltaAeropuerto();
            conducir();
            this.capacidad = 0;        //Se vuelve a poner la capacidad a 0 del bus para simular que se han bajado todos los pasajeros
        }
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
            pasajerosAeropuerto.incrementAndGet();
        }
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
        if (pasajerosAeropuerto.get() == 0){                
            this.capacidad = 0;                             //No se sube ningún pasajero, porque no hay en el aeropuerto
        }else if (pasajerosAeropuerto.get()<=pasajeros){    
            this.capacidad=pasajerosAeropuerto.get();       //Se suben todos los disponibles, que son menos de los que se piden
            pasajerosAeropuerto.set(0);              
        }else{
            this.capacidad=pasajeros;                       //Coge los pasajeros que se han idicado
            pasajerosAeropuerto.set(pasajerosAeropuerto.get()-pasajeros);
        }
    }
}
