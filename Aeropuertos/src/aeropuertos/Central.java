/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import interfaz.Menu;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFrame;


/**
 *
 * @author lucia
 */
public abstract class Central {
    
    // Variables
    private static String nombreArchivo = "evolucionAeropuerto.txt";
    private static String encoding = "UTF-8";
    private static Aeropuerto madrid;
    private static Aeropuerto barcelona;
    private static Log log;
    private static Menu menu;
    private static JFrame frameActual;
    
    private static Semaphore semBusC = new Semaphore(1);
    private static Semaphore semBusA = new Semaphore(1);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        log = new Log(nombreArchivo, encoding);
        
        madrid = new Aeropuerto("Madrid",log);
        barcelona = new Aeropuerto("Barcelona",log);
        
        menu = new Menu();
        menu.setVisible(true);
        
        iniciarCentral();
    }
    
    // Métodos
    /**
     * Guarda los datos y finaliza la aplicación.
     */
    public static void salir() {
        System.exit(0);
    }
    
    /**
     * Inicia los hilos aviones y buses
     * @param
     */
    public static void iniciarCentral(){
        HiloAux hiloAviones = new HiloAux(true, madrid, barcelona, log);
        HiloAux hiloAutobuses = new HiloAux(false, madrid, barcelona, log);

        hiloAviones.start();
        hiloAutobuses.start();
    }
    
    /**
     * Da los pasajeros del aeropuerto determinado
     * @param aeropuerto
     * @return AtomicInteger, el contador de los pasajeros
     */
    public static AtomicInteger getPasajeros(Aeropuerto aeropuerto){
        return aeropuerto.getPasajerosAeropuerto();
    }
    
    /**
     * Suma la cantidad dada a los pasajeros del aeropuerto (Puede ser un número negativo)
     * y lo actualiza en el Menu del aeropuerto
     * @param pasajeros
     * @param aeropuerto 
     */
    public static synchronized void sumarPasajeros(int pasajeros, Aeropuerto aeropuerto){
        menu.actualizarPasajeros(aeropuerto.getPasajerosAeropuerto().addAndGet(pasajeros), aeropuerto.getNombre());
    }
    
    /**
     * Fija ....
     * @param pasajeros
     * @param aeropuerto 
     */
    public static synchronized void fijarPasajeros(int pasajeros, Aeropuerto aeropuerto){
        aeropuerto.getPasajerosAeropuerto().set(pasajeros);
        menu.actualizarPasajeros(aeropuerto.getPasajerosAeropuerto().get(), aeropuerto.getNombre());
    }
    
    public static void mostrarBusCiudad(Bus bus) throws InterruptedException{
        semBusC.acquire();
        
        menu.actualizarBusCiudad(bus, bus.getAeropuerto().getNombre());
        
        semBusC.release();
    }
    
    public static void mostrarBusAeropuerto(Bus bus) throws InterruptedException{
        semBusA.acquire();
        
        menu.actualizarBusAeropuerto(bus, bus.getAeropuerto().getNombre());
        
        semBusA.release();
    }
    
    public static void dormir(int inicioMiliseg, int finalMiliseg){
        int tiempo = inicioMiliseg + (int) ((finalMiliseg - inicioMiliseg) * Math.random());
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException ex) {
        }
    }
    
//    /**
//     * Cambia de Frame cerrando el anterior
//     *
//     * @param nombreFrame
//     */
//    public static void showFrame(String nombreFrame) {
//        if (frameActual != null) {
//            frameActual.setVisible(false);
//            frameActual.dispose();
//            frameActual = null;
//        }
//        try {
//            Class<?> tempClass = Class.forName("interfaz." + nombreFrame);
//            Constructor<?> ctor = tempClass.getConstructor();
//            frameActual = (JFrame) ctor.newInstance();
//            frameActual.setVisible(true);
//        } catch (Exception e) {
//            System.out.println("Error accediendo al nuevo Frame: " + nombreFrame + "\nError: " + e.getCause());
//        }
//    }
}
