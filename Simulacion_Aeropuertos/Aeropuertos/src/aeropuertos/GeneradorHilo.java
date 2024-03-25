/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;
import java.util.Random;

/**
 *
 * @author sandr
 */
public class GeneradorHilo extends Thread {
    private boolean esAvion;
    Random random = new Random();
    
    public GeneradorHilo(boolean avion){
        this.esAvion = avion;
    }
    @Override
    public void run(){
       if (esAvion){
           generarAviones();
       }else{
           generarBuses();
       }
    }
    
    //Genera 8000 aviones con espera entre cada creación de 1-3s
    public void generarAviones(){
        int milisegAvion = 1000+random.nextInt(2000);
        for (int i=0;i<8000;i++){
            Avion avion = new Avion(i);
            avion.start();
            try {
            Thread.sleep(milisegAvion);
            } catch (InterruptedException e) {
                System.err.println("El sleep fue interrumpido");
            }
        } 
    }

    //Genera 4000 aviones con espera entre cada creación de 0.5-1s    
    public void generarBuses(){
        int milisegBus = 500+random.nextInt(500);
        for (int i=0; i<4000;i++){
            Bus bus = new Bus(i);
            bus.start();
            try {
                Thread.sleep(milisegBus);
            } catch (InterruptedException e) {
                System.err.println("El sleep fue interrumpido");
            }
        }
        
    }
}
