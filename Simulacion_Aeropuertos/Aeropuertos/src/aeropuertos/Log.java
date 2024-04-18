/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;
import java.io.*;
/**
 *
 * @author lucia
 */
public class Log {
    
    private String nombreArchivo;
    private String encoding;
    private PrintWriter writer;

    public Log(String nombre, String encoding){
        this.nombreArchivo = nombre;
        this.encoding = encoding;
        try{
            this.writer = new PrintWriter(nombreArchivo, encoding);
        } catch (IOException e) {}
    }
    
    public void escribirArchivo(String linea){
        writer.println(linea);
    }
    
}
