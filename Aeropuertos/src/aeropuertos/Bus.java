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
            //Recoger de 0-50 pasajeros durante 2-5 segundos en la ciudad
            numPasajeros = random.nextInt(50);
            Central.dormir(2000, 5000);
            log.escribirArchivo("Bus " + id + " recoge " + numPasajeros + " pasajeros de la ciudad.", nombreAeropuerto);

            //Esperar 5-10s para conducir
            Central.mostrarBusAeropuerto(this);
            Central.dormir(5000, 10000);
            log.escribirArchivo("Bus " + id + " conduce.", nombreAeropuerto);

            //Deja a todos los pasajeros en el aeropuerto
            Central.sumarPasajeros(numPasajeros, aeropuerto);
            log.escribirArchivo("Bus " + this.id + " deja " + numPasajeros + " pasajeros en el aeropuerto.", nombreAeropuerto);

            //Recoger de 0-50 pasajeros durante 2-5 segundos en el aeropuerto
            int pasajeros = random.nextInt(50);
            Central.dormir(2000, 5000);
            numPasajeros = aeropuerto.getPasajerosDisponibles(pasajeros);
            log.escribirArchivo("Bus " + this.id + " recoge " + pasajeros + " pasajeros del aeropuerto.", nombreAeropuerto);

            //Esperar 5-10s para conducir
            Central.mostrarBusCiudad(this);
            Central.dormir(5000, 10000);
            log.escribirArchivo("Bus " + id + " conduce.", nombreAeropuerto);

            //Deja a todos los pasajeros en la ciudad
            log.escribirArchivo("Bus " + id + " deja " + numPasajeros + " pasajeros en la ciudad.", nombreAeropuerto);
        }
    }

    // Métodos
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
