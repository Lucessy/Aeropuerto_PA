/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aeropuertos;
import java.util.Random;

/**
 *
 * @author sandr
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GeneradorHilo hiloAviones = new GeneradorHilo(true);
        GeneradorHilo hiloAutobuses = new GeneradorHilo(false);
        Random random = new Random();
        
        hiloAviones.start();
        hiloAutobuses.start();
        /*int miliseg;
        
        //Hilo que 
        do{
            hilo.start();
            miliseg = 1000+random.nextInt(2000);
            try{
                Thread.sleep(miliseg);
            }catch (InterruptedException e){
                System.err.println("El sleep fue interrumpido");
            }
            hilo.interrupt();
        }while(hilo.isAlive());*/
    }
}
