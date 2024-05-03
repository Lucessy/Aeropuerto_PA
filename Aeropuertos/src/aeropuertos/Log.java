package aeropuertos;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Log {

    private String nombreArchivo;
    private String encoding;
    private PrintWriter writer;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Lock lockLog = new ReentrantLock(true);

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

    public void escribirArchivo(String evento, String aeropuerto) {
        lockLog.lock();
        try {
            LocalDateTime timestamp = LocalDateTime.now();
            String mensaje = "[" + timestamp.format(formatter) + "] " + aeropuerto + ": " + evento;
            writer.println(mensaje);
            writer.flush(); // Asegurar que los datos se escriban en el archivo inmediatamente
        } finally {
            lockLog.unlock();
        }
    }

    public synchronized void escribirArchivo(String evento) {
        lockLog.lock();
        try {
            // Comentarios del sistema, no del ciclo de vida de buses y aviones
            LocalDateTime timestamp = LocalDateTime.now();
            String mensaje = "[" + timestamp.format(formatter) + "] " + ": " + evento;
            writer.println(mensaje);
            writer.flush(); // Asegurar que los datos se escriban en el archivo inmediatamente
        } finally {
            lockLog.unlock();
        }
    }

    public void cerrar() {
        if (writer != null) {
            writer.close();
        }
    }
}
