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
        this.embarcar = true;//Si está a true es que el avion tiene que embarcar, sino desembarcar. Todos los aviones comienzan teniendo que embarcar
    }

    // Métodos
    public void run() {
        //Solo cuando se crea el avión
        aeropuerto.hangar(this); 
        while(true){
            try {
                // PROCESO DE EMBARCAR
//                aeropuerto.areaEstacionamiento(this);
//                aeropuerto.puertasEmbarque(numPasajeros); // Embarcará porque pues SI XD
                Central.dormir(1000, 5000); //Comprobaciones antes de entrar a pista 1-5seg.
                
                // PROCESO OBTENER PISTA PARA SALIR
                posicionPista = aeropuerto.areaRodaje();  //Pide pista libre y devuelve la posición de la pista 1-4
                log.escribirArchivo("El avión con id "+this.id+" ha entrado en la pista "+(posicionPista+1), nombreAeropuerto);
                Central.dormir(1000, 3000);//Últimas comprobaciones en pista 1-3seg.
                log.escribirArchivo("El avión ha terminado de hacer las últimas comprobaciones", nombreAeropuerto);
                Central.dormir(1000, 5000);  //Tiempo de despegue 1-5seg.
                log.escribirArchivo("El avión ha despegado con éxito", nombreAeropuerto);
                numVuelos+=1;//Registro del número de vuelos para el taller
                
                // PROCESO DE ACCEDER A LA AEROVIA
                aeropuerto.liberarPista(posicionPista);
                log.escribirArchivo("La pista "+(posicionPista+1)+" ha sido liberada", nombreAeropuerto);
                
                // ACCEDER AEROVIA
                aeropuerto.accederAerovia();
                Central.dormir(15000, 30000);
                posicionPista = aeropuerto.solicitarPista(); //Espera entre 1-5 seg hasta conseguirla
                Central.dormir(1000, 5000); //Dura 1-5 seg en aterrizar
                aeropuerto.areaRodaje(); //Accede para pedir una puerta de embarque para desembarcar
                // dura entre 3-5 seg entre la pista y las puertas de embarque
                Central.dormir(1000,5000); //Descarga de los pasajeros
                
                aeropuerto.areaEstacionamiento(this);
                Central.dormir(1000,5000); //Comprobaciones de los pilotos
                
                aeropuerto.taller(this);
                
                if(random.nextInt(2) == 0){
                    aeropuerto.hangar(this);
                }

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
