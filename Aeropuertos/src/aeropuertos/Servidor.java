package aeropuertos;

import interfaz.Menu;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Servidor {

    // Variables
    private static String nombreArchivo = "evolucionAeropuerto.txt";
    private static String encoding = "UTF-8";
    private static Aeropuerto madrid;
    private static Aeropuerto barcelona;
    private static Log log;
    private static Menu menu;

    private static Semaphore semBusC = new Semaphore(1);
    private static Semaphore semBusA = new Semaphore(1);
    private static Semaphore semActCampo = new Semaphore(1, true);
    private static Semaphore semActCampoSol = new Semaphore(1, true);

    private static Queue<Avion> aviones = new ConcurrentLinkedQueue();
    private static Queue<Bus> buses = new ConcurrentLinkedQueue();

    private static boolean estaPausado;
    private static Lock lockPausa = new ReentrantLock();

    // CONEXIÓN SOCKET
    private static ServerSocket servidor;
    private static Socket conexion;
    private static DataOutputStream salida;
    private static DataInputStream entrada;
    private static String mensajeMadrid;
    private static String mensajeBarcelona;
    private static List<String> listaMadrid = new ArrayList<>();
    private static List<String> listaBarce = new ArrayList<>();
    private static String botonesPistas;
    private static Boolean[] listaPistasMadrid = new Boolean[4];
    private static Boolean[] listaPistasBarcelona = new Boolean[4];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        log = new Log(nombreArchivo, encoding);

        madrid = new Aeropuerto("Madrid", log);
        barcelona = new Aeropuerto("Barcelona", log);

        menu = new Menu(log);
        menu.setVisible(true);
        estaPausado = false;

        iniciarCentral();

        try {
            // Crea la conexión con el puerto dado
            servidor = new ServerSocket(5000);
            System.out.println("Servidor arrancando. . .");
            conexion = servidor.accept();
            // Crea los canales de entrada y salida
            salida = new DataOutputStream(conexion.getOutputStream());
            entrada = new DataInputStream(conexion.getInputStream());

            while (true) {
                // Recibimos si alguna puerta esta cerrada o abierta
                botonesPistas = entrada.readUTF();
                listaPistasMadrid = stringToArray(botonesPistas);
                madrid.setListaBotonPista(listaPistasMadrid);
                menu.actualizarColor(true, listaPistasMadrid);

                botonesPistas = entrada.readUTF();
                listaPistasBarcelona = stringToArray(botonesPistas);
                barcelona.setListaBotonPista(listaPistasBarcelona);
                menu.actualizarColor(false, listaPistasBarcelona);

                // Enviamos la información del aeropuerto Madrid
                listaMadrid.clear();
                listaMadrid.add(String.valueOf(madrid.getPasajerosAeropuerto().get()));
                listaMadrid.add(String.valueOf(madrid.getHangar().size()));
                listaMadrid.add(String.valueOf(madrid.getEstacionamiento().size()));
                listaMadrid.add(String.valueOf(madrid.getRodaje().size()));
                listaMadrid.add(String.valueOf(madrid.getTaller().size()));
                mensajeMadrid = String.join(",", listaMadrid);

                salida.writeUTF("Madrid");
                salida.writeUTF(mensajeMadrid);
                salida.writeUTF(queueToString(madrid.getAerovia()));

                // Enviamos la información del aeropuerto Barcelona
                listaBarce.clear();
                listaBarce.add(String.valueOf(barcelona.getPasajerosAeropuerto().get()));
                listaBarce.add(String.valueOf(barcelona.getHangar().size()));
                listaBarce.add(String.valueOf(barcelona.getEstacionamiento().size()));
                listaBarce.add(String.valueOf(barcelona.getRodaje().size()));
                listaBarce.add(String.valueOf(barcelona.getTaller().size()));
                mensajeBarcelona = String.join(",", listaBarce);

                salida.writeUTF("Barcelona");
                salida.writeUTF(mensajeBarcelona);
                salida.writeUTF(queueToString(barcelona.getAerovia()));

            }
        } catch (IOException e) {
        }
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
     *
     * @param
     */
    public static void iniciarCentral() {
        HiloAux hiloAviones = new HiloAux(true, madrid, barcelona, log);
        HiloAux hiloAutobuses = new HiloAux(false, madrid, barcelona, log);

        hiloAviones.start();
        hiloAutobuses.start();
    }

    /**
     *  Agrega el avión a la lista
     * @param avion
     */
    public static void agregarAvion(Avion avion) {
        aviones.offer(avion);
    }

    /**
     *  Agrega el bus a la lista
     * @param bus
     */
    public static void agregarBus(Bus bus) {
        buses.offer(bus);
    }

    /**
     * Suma la cantidad dada a los pasajeros del aeropuerto (Puede ser un número
     * negativo) y lo actualiza en el Menu del aeropuerto
     *
     * @param pasajeros
     * @param aeropuerto
     */
    public static synchronized void sumarPasajeros(int pasajeros, Aeropuerto aeropuerto) {
        menu.actualizarPasajeros(aeropuerto.getPasajerosAeropuerto().addAndGet(pasajeros), aeropuerto.getNombre());
    }

    /**
     *
     * Actualiza el número de pasajeros del aeropuerto indicado en la interfaz
     * Menu
     *
     * @param pasajeros
     * @param aeropuerto
     */
    public static synchronized void actualizarPasajerosAeropuerto(int pasajeros, Aeropuerto aeropuerto) {
        menu.actualizarPasajeros(pasajeros, aeropuerto.getNombre());
    }

    /**
     *  Muestra el bus dado en la interfaz hacía la Ciudad
     * @param bus
     */
    public static synchronized void mostrarBusCiudad(Bus bus) {
        try {
            semBusC.acquire();

            menu.actualizarBusCiudad(bus, bus.getAeropuerto().getNombre());

            semBusC.release();
        } catch (InterruptedException ex) {
        }
    }

    /**
     *  Muestra el bus dado en la interfaz hacía el Aeropuerto
     * @param bus
     */
    public static synchronized void mostrarBusAeropuerto(Bus bus) {
        try {
            semBusA.acquire();

            menu.actualizarBusAeropuerto(bus, bus.getAeropuerto().getNombre());

            semBusA.release();
        } catch (InterruptedException ex) {
        }
    }

    /**
     *  Actualiza los aviones dado un:
     * @param avion 
     * @param agregar si se añade o se quita de la lista, en la interfaz
     * @param textField el nombre del campo donde se va a modificar
     * @param listaAviones que se escribirán en la función
     * @param aeropuerto donde se actualizará los valores nuevos
     */
    public static void actualizarAviones(Avion avion, boolean agregar, String textField, Queue<Avion> listaAviones, String aeropuerto) {
        try {
            semActCampo.acquire();

            if (agregar) {
                listaAviones.offer(avion);
            } else {
                listaAviones.remove(avion);
            }

            menu.actualizarCampoAvion(textField, listaAviones, aeropuerto);

            semActCampo.release();
        } catch (InterruptedException ex) {
        }
    }

    /**
     *  Actualiza el avión dado un
     * @param textField el nombre del campo donde se va a modificar
     * @param idAvion el id del avión
     * @param aeropuerto donde se actualizará los valores nuevos
     */
    public static void actualizarAvionesSolitario(String textField, String idAvion, String aeropuerto) {
        try {
            semActCampoSol.acquire();

            menu.actualizarCampoAvionSolitario(textField, idAvion, aeropuerto);

            semActCampoSol.release();
        } catch (InterruptedException ex) {
        }
    }


    /**
     * Consideramos que estará pausado cuando los hilos duermen, por lo que se
     * pone el flag estaPausado en true y se hace un lock para dejar a los hilos
     * bloqueados.
     */
    public static void pausarSistema() {
        estaPausado = true;
        lockPausa.lock();
    }

    /**
     *  Reanuda el sistema desbloqueando el lock adquirido.
     */
    public static void reanudarSistema() {
        estaPausado = false;
        lockPausa.unlock();
    }

    /**
     *  Duerme los hilos dado un valor de inicio y de final en milisegundos,
     * si el sistema está pausado, el hilo se quedará en un Lock
     * @param inicioMiliseg
     * @param finalMiliseg
     */
    public static void dormir(int inicioMiliseg, int finalMiliseg) {
        int tiempo = inicioMiliseg + (int) ((finalMiliseg - inicioMiliseg) * Math.random());
        try {
            Thread.sleep(tiempo);
            if (estaPausado) {
                lockPausa.lock();
                lockPausa.unlock();
            }
        } catch (InterruptedException ex) {
        }
    }

    /**
     *  Método para pasar una String a un Array
     * @param cadena
     * @return
     */
    private static Boolean[] stringToArray(String cadena) {
        // Dividir la cadena en subcadenas utilizando la coma como delimitador
        String[] subcadenas = cadena.split(",");

        // Crear un nuevo array de Boolean con la misma longitud que las subcadenas
        Boolean[] array = new Boolean[subcadenas.length];

        // Convertir cada subcadena en un valor Boolean y almacenarlo en el array
        for (int i = 0; i < subcadenas.length; i++) {
            array[i] = Boolean.valueOf(subcadenas[i]);
        }

        return array;
    }

    /**
     *  Método para pasar una Queue a una String
     * @param cola
     * @return
     */
    private static String queueToString(Queue<Avion> cola) {
        // Iterar sobre cada elemento de la cola
        String texto = "";
        int i = 0;
        for (Avion avion : cola) {
            texto += avion.getIdAvion();
            if (i < cola.size() - 1) {
                texto += ",";
            }
            i++;
        }

        return texto;
    }
}
