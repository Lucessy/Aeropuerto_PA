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

    private int capacidad;
    private String id;
    private Aeropuerto aeropuerto;
    private int numPasajeros;
    private int tiempo;
    private Random random;
    private int posicionPista;

    //Constructor
    public Avion(String id, Aeropuerto aeropuerto, int capacidad) {
        this.capacidad = capacidad;
        this.id = id;
        this.aeropuerto = aeropuerto;
    }

    // Métodos
    public void run() {
        while(true){
            try {
                tiempo = 1000+random.nextInt(4000);
                sleep(tiempo);  //Comprobaciones antes de entrar a pista 1-5seg.
                posicionPista = aeropuerto.getPista();  //Pide pista libre y devuelve la posición de la pista 1-4
                System.out.println("El avión con id"+this.id+"ha entrado en la pista"+(posicionPista+1));
                tiempo = 1000+random.nextInt(2000); //Últimas comprobaciones en pista 1-3seg.
                sleep(tiempo);
                System.out.println("El avión ha terminado de hacer las últimas comprobaciones");
                tiempo = 1000+random.nextInt(4000);
                sleep(tiempo);  //Tiempo de despegue 1-5seg.
                System.out.println("El avión ha despegado con éxito");
                aeropuerto.liberarPista(posicionPista);
                System.out.println("La pista"+(posicionPista+1)+"ha sido liberada");

            } catch (InterruptedException ex) {
                System.out.println(ex);
            }

        }
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
    
}
