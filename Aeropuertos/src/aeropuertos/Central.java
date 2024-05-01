package aeropuertos;

import interfaz.Menu;
import interfaz.MenuRemoto;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public abstract class Central {
    
    // Variables
    private static String nombreArchivo = "evolucionAeropuerto.txt";
    private static String encoding = "UTF-8";
    private static Aeropuerto madrid;
    private static Aeropuerto barcelona;
    private static Log log;
    private static Menu menu;
    private static MenuRemoto menuRemoto;
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
        
        menuRemoto = new MenuRemoto();
        
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
     * Inicia los hilos que crearán los aviones y buses
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
     * 
     * Actualiza el número de pasajeros del aeropuerto indicado
     * en la interfaz Menu
     * @param pasajeros
     * @param aeropuerto 
     */
    public static void actualizarPasajerosAeropuerto(int pasajeros, Aeropuerto aeropuerto){
        menu.actualizarPasajeros(pasajeros, aeropuerto.getNombre());
    }
    
    
    public static void mostrarBusCiudad(Bus bus){
        try {
            semBusC.acquire();
            
            menu.actualizarBusCiudad(bus, bus.getAeropuerto().getNombre());
            
            semBusC.release();
        } catch (InterruptedException ex) {
        }
    }
    
    public static void mostrarBusAeropuerto(Bus bus){
        try {
            semBusA.acquire();
            
            menu.actualizarBusAeropuerto(bus, bus.getAeropuerto().getNombre());
            
            semBusA.release();
        } catch (InterruptedException ex) {
        }
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
