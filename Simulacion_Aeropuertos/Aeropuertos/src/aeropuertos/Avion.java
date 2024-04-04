/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author sandr
 */
public class Avion extends Thread {

    private int capacidad;
    private String id;
    private Aeropuerto aeropuerto;
    private AtomicInteger numPasajeros;

    //Constructor
    public Avion(String id, Aeropuerto aeropuerto, int capacidad) {
        this.capacidad = capacidad;
        this.id = id;
        this.aeropuerto = aeropuerto;
    }

    // Métodos
    public void run() {

    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getIdAvion() {
        return id;
    }

    public void setIdAvion(String id) {
        this.id = id;
    }

}
