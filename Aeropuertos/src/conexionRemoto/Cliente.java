package conexionRemoto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Cliente {

    private static Socket cliente;
    private static DataInputStream entrada;
    private static DataOutputStream salida;
    private static String mensaje;
    private static String datosNum;
    private static String datosAerovia;

    private static MenuRemoto menuR;

    public Cliente() {
    }

    public static void main(String[] args) {
        menuR = new MenuRemoto();
        menuR.setVisible(true);

        try {
            //Creamos el socket para conectarnos al puerto 5000 
            cliente = new Socket(InetAddress.getLocalHost(), 5000);
            System.out.println("Conectando. . .");
            //Creamos los canales de E/S
            entrada = new DataInputStream(cliente.getInputStream());
            salida = new DataOutputStream(cliente.getOutputStream());

            while (true) {
                // Enviamos el estado de los botones de las pistas
                salida.writeUTF(menuR.obtenerDisPistas("Madrid"));
                salida.writeUTF(menuR.obtenerDisPistas("Barcelona"));

                // Recibimos los datos actualizados
                mensaje = entrada.readUTF();
                datosNum = entrada.readUTF();
                datosAerovia = entrada.readUTF();

                menuR.actualizarCampoNumerico(datosNum, datosAerovia, mensaje);

                mensaje = entrada.readUTF();
                datosNum = entrada.readUTF();
                datosAerovia = entrada.readUTF();

                menuR.actualizarCampoNumerico(datosNum, datosAerovia, mensaje);
            }

//            //Cerramos los flujos de entrada y salida
//            entrada.close();
//            cliente.close(); //Cerramos la conexión
        } catch (IOException e) {
        }
    }

}
