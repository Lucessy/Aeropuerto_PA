/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aeropuertos;

/**
 *
 * @author sandr
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String nombreArchivo = "evolucionAeropuerto.txt";
        String encoding = "UTF-8";
        
        Log log = new Log(nombreArchivo, encoding);
        
        Aeropuerto madrid = new Aeropuerto("Madrid",log);
        Aeropuerto barcelona = new Aeropuerto("Barcelona",log);

        HiloAux hiloAviones = new HiloAux(true, madrid, barcelona, log);
        HiloAux hiloAutobuses = new HiloAux(false, madrid, barcelona, log);

        hiloAviones.start();
        hiloAutobuses.start();
        
        
    }
}
