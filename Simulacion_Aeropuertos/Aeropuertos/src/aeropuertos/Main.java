/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aeropuertos;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author sandr
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Aeropuerto madrid = new Aeropuerto();
        Aeropuerto barcelona = new Aeropuerto();
        HiloAux hiloAviones = new HiloAux(true,madrid, barcelona);
        HiloAux hiloAutobuses = new HiloAux(false,madrid,barcelona);
        Random random = new Random();
        AtomicInteger capacidadAeropuerto = new AtomicInteger(0);
        
        hiloAviones.start();
        hiloAutobuses.start();
        System.out.print("aviones madrid"+ madrid.getAviones().toString());
        System.out.print("aviones barcelona"+ barcelona.getAviones().toString());
        System.out.print("Buses madrid"+madrid.getBuses().toString());
        System.out.print("Buses barcelona"+barcelona.getBuses().toString());
        
    }
}
