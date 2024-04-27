/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import interfaz.Menu;
import java.lang.reflect.Constructor;
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

    public static void iniciarCentral(){
        HiloAux hiloAviones = new HiloAux(true, madrid, barcelona, log);
        HiloAux hiloAutobuses = new HiloAux(false, madrid, barcelona, log);

        hiloAviones.start();
        hiloAutobuses.start();
    }
    
    public static AtomicInteger getPasajeros(Aeropuerto aeropuerto){
        return aeropuerto.getPasajerosAeropuerto();
    }
    
    public static void sumarPasajeros(int pasajeros, Aeropuerto aeropuerto){
        menu.actualizarPasajeros(aeropuerto.getPasajerosAeropuerto().addAndGet(pasajeros), aeropuerto.getNombre());
    }
    
    public static void fijarPasajeros(int pasajeros, Aeropuerto aeropuerto){
        aeropuerto.getPasajerosAeropuerto().set(pasajeros);
        menu.actualizarPasajeros(aeropuerto.getPasajerosAeropuerto().get(), aeropuerto.getNombre());
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
