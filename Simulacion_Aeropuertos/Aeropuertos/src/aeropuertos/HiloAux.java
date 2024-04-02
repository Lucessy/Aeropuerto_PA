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
public class HiloAux extends Thread {

    // Atributos
    private boolean esAvion;

    private Aeropuerto madrid;
    private Aeropuerto barcelona;

    private String stringNumId, id;
    private int num1, num2;
    private char letra1, letra2;

    private Random random = new Random();

    // Constructor 
    public HiloAux(boolean avion, Aeropuerto madrid, Aeropuerto barcelona) {
        this.esAvion = avion;
        this.madrid = madrid;
        this.barcelona = barcelona;
    }

    // Métodos
    @Override
    public void run() {
        if (esAvion) {
            generarAviones();
            System.out.println("Aviones generados");

        } else {
            generarBuses();
            System.out.println("Buses generados");

        }
    }

    //Genera 8000 aviones con espera entre cada creación de 1-3s
    public void generarAviones() {
        int milisegAvion = 1000 + random.nextInt(2000);
        for (int i = 0; i < 8000; i++) {
            num1 = random.nextInt(26);
            num2 = random.nextInt(26);
            letra1 = (char) ('A' + num1);
            letra2 = (char) ('A' + num2);

            stringNumId = String.format("%04d", i + 1);
            id = "" + letra1 + letra2 + "-" + stringNumId;
            int capacidadAvion = 100 + random.nextInt(200); // Asigna un valor aleatorio entre 100 y 300 pasajeros de capacidad
            if ((i + 1) % 2 == 0) {             //Añade en el array de aviones de la clase aeropuerto los id que son pares en la instancia de madrid y los que son 
                Avion avion = new Avion(id, madrid, capacidadAvion);
                madrid.addAvion(avion);   //impares en la instancia de barcelona
                avion.start();
            } else {
                Avion avion = new Avion(id, barcelona, capacidadAvion);
                barcelona.addAvion(avion);
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
    public void generarBuses() {
        int milisegBus = 500 + random.nextInt(500);
        for (int i = 0; i < 4000; i++) {
            stringNumId = String.format("%04d", i + 1);   //Añade 0's si es necesario para el formato XXXX
            id = "B-" + stringNumId;
            if ((i + 1) % 2 == 0) {             //Añade en el array de buses de la clase aeropuerto los id que son pares en la instancia de madrid y los que son 
                Bus bus = new Bus(id, madrid);
                madrid.addBus(bus);   //impares en la instancia de barcelona
                bus.start();
            } else {
                Bus bus = new Bus(id, barcelona);
                barcelona.addBus(bus);
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
