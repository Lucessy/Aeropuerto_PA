/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    private String nombreArchivo;
    private String encoding;
    private PrintWriter writer;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Log(String nombre, String encoding) {
        this.nombreArchivo = nombre;
        this.encoding = encoding;
        try {
            this.writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(nombreArchivo, true),
                    encoding));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void escribirArchivo(String evento, String aeropuerto) {
        LocalDateTime timestamp = LocalDateTime.now();
        String mensaje = "[" + timestamp.format(formatter) + "] " + aeropuerto + ": " + evento;
        writer.println(mensaje);
        writer.flush(); // Asegurar que los datos se escriban en el archivo inmediatamente
    }
    
    public synchronized void escribirArchivo(String evento) { // Comentarios del sistema, no del ciclo de vida de buses y aviones
        LocalDateTime timestamp = LocalDateTime.now();
        String mensaje = "[" + timestamp.format(formatter) + "] " + ": " + evento;
        writer.println(mensaje);
        writer.flush(); // Asegurar que los datos se escriban en el archivo inmediatamente
    }

    public void cerrar() {
        if (writer != null) {
            writer.close();
        }
    }
}

