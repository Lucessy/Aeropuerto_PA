/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import static java.lang.Math.random;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sandr
 */
public class Avion extends Thread {
    
    private Log log;
    private int capacidad;
    private String id;
    private Aeropuerto aeropuerto;
    private int numPasajeros;
    private int tiempo;
    private Random random;
    private int posicionPista;
    private String nombreAeropuerto;
    private int numVuelos;
    private boolean embarcar;

    //Constructor
    public Avion(String id, Aeropuerto aeropuerto, int capacidad, Log log) {
        this.capacidad = capacidad;
        this.id = id;
        this.aeropuerto = aeropuerto;
        this.log = log;
        this.nombreAeropuerto = aeropuerto.getNombre();
        this.numVuelos = 0;
        this.embarcar = true;//Si está a true es que el avion tiene que embarcar, sino desembarcar
    }

    // Métodos
    public void run() {
//        while(true){
//            try {
//                /*Hangar -> Puertas de embarque */
//                
//                tiempo = 1000+random.nextInt(4000);
//                sleep(tiempo);  //Comprobaciones antes de entrar a pista 1-5seg.
//                posicionPista = aeropuerto.areaRodaje();  //Pide pista libre y devuelve la posición de la pista 1-4
//                log.escribirArchivo("El avión con id "+this.id+" ha entrado en la pista "+(posicionPista+1), nombreAeropuerto);
//                
//                tiempo = 1000+random.nextInt(2000); //Últimas comprobaciones en pista 1-3seg.
//                sleep(tiempo);
//                log.escribirArchivo("El avión ha terminado de hacer las últimas comprobaciones", nombreAeropuerto);
//                
//                tiempo = 1000+random.nextInt(4000);
//                sleep(tiempo);  //Tiempo de despegue 1-5seg.
//                log.escribirArchivo("El avión ha despegado con éxito", nombreAeropuerto);
//                
//                numVuelos+=1;//Registro del número de vuelos para el taller
//                
//                aeropuerto.liberarPista(posicionPista);
//                log.escribirArchivo("La pista "+(posicionPista+1)+" ha sido liberada", nombreAeropuerto);
//
//            } catch (InterruptedException ex) {
//                System.out.println(ex);
//            }
//        }
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

    public int getNumPasajeros() {
        return numPasajeros;
    }

    public void setNumPasajeros(int numPasajeros) {
        this.numPasajeros = numPasajeros;
    }

    public int getNumVuelos() {
        return numVuelos;
    }

    public void setNumVuelos(int numVuelos) {
        this.numVuelos = numVuelos;
    }

    public boolean isEmbarcar() {
        return embarcar;
    }

    public void setEmbarcar(boolean embarcar) {
        this.embarcar = embarcar;
    }
    
    
    
    
}
