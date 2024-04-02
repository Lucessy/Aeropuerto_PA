/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author sandr
 */
public class Aeropuerto {

    private BlockingQueue aviones = new LinkedBlockingQueue();
    private BlockingQueue buses = new LinkedBlockingQueue();
    private AtomicInteger pasajerosAeropuerto;
    private String nombre;

    // Constructor
    public Aeropuerto(String nombre) {
        this.nombre = nombre;
        this.pasajerosAeropuerto = new AtomicInteger(0);
    }

    // Métodos
    public void añadirBus(Bus bus) { //Pasar objeto Bus
        try {
            buses.put(bus);

            System.out.println("Bus " + bus.getIdBus() + " es creado.");

        } catch (InterruptedException ex) {
            System.out.println("Error en la inserción del bus");
        }

    }

    public void añadirAvion(Avion avion) { //Pasar objeto Avion
        try {
            aviones.put(avion);

            System.out.println("Avion " + avion.getIdAvion() + " es creado.");

        } catch (InterruptedException ex) {
            System.out.println("Error en la inserción del avión");
        }
    }

    public BlockingQueue getAviones() {
        return aviones;
    }

    public BlockingQueue getBuses() {
        return buses;
    }

    public AtomicInteger getPasajerosAeropuerto() {
        return pasajerosAeropuerto;
    }

}
