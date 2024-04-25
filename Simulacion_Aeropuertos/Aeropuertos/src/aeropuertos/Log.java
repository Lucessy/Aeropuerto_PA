/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import java.io.*;

public class Log {

    private String nombreArchivo;
    private String encoding;
    private PrintWriter writer;

    public Log(String nombre, String encoding) {
        this.nombreArchivo = nombre;
        this.encoding = encoding;
        try {
            this.writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(nombreArchivo, true), encoding));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void escribirArchivo(String linea) {
        if (writer != null) {
            writer.println(linea);
            writer.flush(); // Asegurar que los datos se escriban en el archivo inmediatamente
        } else {
            System.err.println("No se pudo escribir en el archivo porque no se pudo abrir correctamente.");
        }
    }

    public void cerrar() {
        if (writer != null) {
            writer.close();
        }
    }
}
