/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import java.util.ArrayList;

/**
 *
 * @author sandr
 */
public class Aeropuerto {
    private ArrayList<String> aviones = new ArrayList<>();
    private ArrayList<String> buses = new ArrayList<>();
    
    
    public void añadirBus(String id){
        buses.add(id);
    }
    public void añadirAvion(String id){
        aviones.add(id);
    }

    public ArrayList<String> getAviones() {
        return aviones;
    }

    public ArrayList<String> getBuses() {
        return buses;
    }
    
    
}
