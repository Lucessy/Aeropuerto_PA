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
public class HiloAux extends Thread {
    private boolean esAvion;
    //private AtomicInteger capacidadAeropuerto;
    private Aeropuerto madrid;
    private Aeropuerto barcelona;
    private String stringNumId, id;
    private int num1,num2;
    private char letra1,letra2;
    Random random = new Random();
    
    public HiloAux(boolean avion, Aeropuerto madrid, Aeropuerto barcelona){
        this.esAvion = avion;
        this.madrid = madrid;
        this.barcelona = barcelona;
        //this.capacidadAeropuerto = capacidadAeropuerto;
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
            num1 = random.nextInt(26);
            num2 = random.nextInt(26);
            letra1 = (char)('A'+num1);
            letra2 = (char)('A'+num2);

            stringNumId = String.format("%04d", i+1);
            id = ""+letra1+letra2+"-"+stringNumId;
            if((i+1)%2==0){             //Añade en el array de aviones de la clase aeropuerto los id que son pares en la instancia de madrid y los que son 
                madrid.añadirAvion(stringNumId);   //impares en la instancia de barcelona
                Avion avion = new Avion(id, madrid);
                avion.start();
            }else{
                barcelona.añadirAvion(stringNumId);
                Avion avion = new Avion(id, barcelona);
                avion.start();
            }
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
            stringNumId = String.format("%04d", i+1);   //Añade 0's si es necesario para el formato XXXX
            id = "B-"+stringNumId;
            if((i+1)%2==0){             //Añade en el array de buses de la clase aeropuerto los id que son pares en la instancia de madrid y los que son 
                madrid.añadirBus(stringNumId);   //impares en la instancia de barcelona
                Bus bus = new Bus(id, madrid.getPasajerosAeropuerto());
                bus.start();
            }else{
                barcelona.añadirBus(stringNumId);
                Bus bus = new Bus(id, barcelona.getPasajerosAeropuerto());
                bus.start();
            }            
            try {
                Thread.sleep(milisegBus);
            } catch (InterruptedException e) {
                System.err.println("El sleep fue interrumpido");
            }
        }
        
    }
}
