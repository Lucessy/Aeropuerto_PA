package conexionRemoto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Cliente {

    public Cliente() {
    }

    public static void main(String[] args) {
        Socket cliente;
        int numeroPuerto;
        DataInputStream entrada;
        DataOutputStream salida;
        String mensaje, respuesta;
        try {
            //Creamos el socket para conectarnos al puerto 5000 
            cliente = new Socket(InetAddress.getLocalHost(), 5000);
            
            //Creamos los canales de E/S
            entrada = new DataInputStream(cliente.getInputStream()); 
            salida = new DataOutputStream(cliente.getOutputStream());
            
            mensaje = "Miguel Sánchez";
            salida.writeUTF(mensaje); //Enviamos un mensaje al servidor
            respuesta = entrada.readUTF(); //Leemos la respuesta
            
            System.out.println("Mi mensaje: " + mensaje);
            System.out.println("Respuesta del Servidor: " + respuesta);
            
            //Cerramos los flujos de entrada y salida
            entrada.close(); 
            salida.close();
            cliente.close(); //Cerramos la conexión
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
