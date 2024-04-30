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
public class Bus extends Thread {

    private Log log;
    private int numPasajeros;
    private String id;
    private Random random = new Random();
    private Aeropuerto aeropuerto;
    private String nombreAeropuerto;

    //Constructor
    public Bus(String id, Aeropuerto aeropuerto, Log log) {
        this.numPasajeros = 0;
        this.id = id;
        this.aeropuerto = aeropuerto;
        this.log = log;
        this.nombreAeropuerto = aeropuerto.getNombre();
    }

    /**
     * Ciclo de vida del bus
     */
    public void run() {
        while (true) {
            llegadaCiudad();
            conducir();
            llegadaAeropuerto(this.numPasajeros);
            conducir();

            log.escribirArchivo("Bus " + this.id + " deja " + this.numPasajeros + " pasajeros en la ciudad.", nombreAeropuerto);

            this.numPasajeros = 0;        //Se vuelve a poner la numPasajeros a 0 del bus para simular que se han bajado todos los pasajeros
        }
    }

    //Recoger de 0-50 pasajeros durante 2-5 segundos
    public void llegadaCiudad() {
        int miliseg = 2000 + random.nextInt(3000);
        this.numPasajeros = random.nextInt(50);
        try {
            Thread.sleep(miliseg);

            log.escribirArchivo("Bus " + this.id + " recoge " + this.numPasajeros + " pasajeros de la ciudad.", nombreAeropuerto);

            Central.mostrarBusCiudad(this);
        } catch (InterruptedException e) {
            log.escribirArchivo("El sleep fue interrumpido", nombreAeropuerto);
        }
    }

    //Esperar 5-10s
    public void conducir() {
        int miliseg = 5000 + random.nextInt(5000);
        try {
            Thread.sleep(miliseg);
            log.escribirArchivo("Bus " + this.id + " conduce.", nombreAeropuerto);

        } catch (InterruptedException e) {
            log.escribirArchivo("El sleep fue interrumpido", nombreAeropuerto);
        }
    }

    //Aumento de la variable atómica del aeropuerto con los pasajeros que llegan
    public void llegadaAeropuerto(int pasajeros) {
        Central.sumarPasajeros(pasajeros, aeropuerto);

        log.escribirArchivo("Bus " + this.id + " deja " + pasajeros + " pasajeros en el aeropuerto.", nombreAeropuerto);

        vueltaAeropuerto();
    }

    //Se esperan de 2-5 segundos y se cogen los pasajeros que se piden o los que haya disponibles si son menos.
    public void vueltaAeropuerto() {
        int pasajeros = random.nextInt(50);
        int miliseg = 2000 + random.nextInt(3000);
        try {
            Thread.sleep(miliseg);

            if (Central.getPasajeros(aeropuerto).get() == 0) {
                this.numPasajeros = 0;                             //   No se sube ningún pasajero, porque no hay en el aeropuerto

            } else if (Central.getPasajeros(aeropuerto).get() <= pasajeros) {
                this.numPasajeros = Central.getPasajeros(aeropuerto).get();       //    Se suben todos los disponibles, que son menos de los que se piden y se iguala a 0
                Central.fijarPasajeros(0, aeropuerto);

            } else {
                this.numPasajeros = pasajeros;                          //  Coge los pasajeros que se han idicado
                Central.sumarPasajeros(-pasajeros, aeropuerto);         //  Decrementa la cantidad de pasajeros total -(pasajeros)
            }

            log.escribirArchivo("Bus " + this.id + " recoge " + pasajeros + " pasajeros del aeropuerto.", nombreAeropuerto);

            Central.mostrarBusAeropuerto(this);

        } catch (InterruptedException e) {
            log.escribirArchivo("El sleep o semáforo fue interrumpido", nombreAeropuerto);
        }
    }

    // Métodos Get y Set
    public int getNumPasajeros() {
        return numPasajeros;
    }

    public void setNumPasajeros(int numPasajeros) {
        this.numPasajeros = numPasajeros;
    }

    public String getIdBus() {
        return id;
    }

    public void setIdBus(String id) {
        this.id = id;
    }

    public Aeropuerto getAeropuerto() {
        return aeropuerto;
    }

    public void setAeropuerto(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

}
