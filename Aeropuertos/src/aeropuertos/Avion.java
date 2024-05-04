package aeropuertos;

import java.util.Random;

public class Avion extends Thread {

    private Log log;
    private int capacidad;
    private String id;
    private Aeropuerto aeropuertoActual;
    private Aeropuerto aeropuertoAntiguo;
    private Aeropuerto barcelona;
    private Aeropuerto madrid;
    private int numPasajeros;
    private Random random;
    private int posicionPista;
    private int posPuerta;
    private int numVuelos;

    //Constructor
    public Avion(String id, Aeropuerto aeropuertoActual, Aeropuerto madrid, Aeropuerto barcelona, int capacidad, Log log) {
        this.id = id;
        this.aeropuertoActual = aeropuertoActual;
        this.madrid = madrid;
        this.barcelona = barcelona;
        this.capacidad = capacidad;
        this.log = log;
        this.numVuelos = 0;
        this.numPasajeros = 0;
        this.random = new Random();
    }

    // Métodos
    public void run() {
        //Solo cuando se crea el avión
        /* ACCEDE HANGAR */
        aeropuertoActual.hangar(this, false);
        log.escribirArchivo("El avión con id " + this.id + " entra en el HANGAR", aeropuertoActual.getNombre());

        while (true) {
            /* ACCEDER ÁREA DE ESTACIONAMIENTO */
            aeropuertoActual.areaEstacionamiento(this);
            log.escribirArchivo("El avión con id " + this.id + " entra en el ÁREA DE ESTACIONAMIENTO", aeropuertoActual.getNombre());

            /* OBTENER PUERTA DE EMBARQUE */
            aeropuertoActual.puertasEmbarque(this);
            log.escribirArchivo("El avión con id " + this.id + " entra en la puerta de EMBARQUE " + (posPuerta + 1), aeropuertoActual.getNombre());

            /* EMBARCAR EL MÁXIMO Nº DE PASAJEROS */
            embarcarPasajeros();
            log.escribirArchivo("El avión con id " + this.id + " ha embarcado " + numPasajeros + " pasajeros", aeropuertoActual.getNombre());
            aeropuertoActual.salirPuertasEmbarque(this);

            /* ACCEDER ÁREA RODAJE */
            aeropuertoActual.areaRodaje(this);
            log.escribirArchivo("El avión con id " + this.id + "(" + numPasajeros + " pasajeros)" + " entra en el ÁREA DE RODAJE", aeropuertoActual.getNombre());
            Servidor.dormir(1000, 5000); //Comprobaciones antes de entrar a pista 1-5seg.

            /* OBTENER PISTA */
            aeropuertoActual.pista(this);    //Pide pista libre
            log.escribirArchivo("El avión con id " + this.id + "(" + numPasajeros + " pasajeros)" + " ha entrado en la PISTA " + (posicionPista + 1), aeropuertoActual.getNombre());
            Servidor.dormir(1000, 3000);//Últimas comprobaciones en pista 1-3seg.
            log.escribirArchivo("El avión con id " + this.id + "(" + numPasajeros + " pasajeros)" + " ha terminado de hacer las últimas comprobaciones", aeropuertoActual.getNombre());

            Servidor.dormir(1000, 5000);  //Tiempo de despegue 1-5seg.
            log.escribirArchivo("El avión con id " + this.id + "(" + numPasajeros + " pasajeros)" + " ha despegado con éxito", aeropuertoActual.getNombre());
            numVuelos += 1;//Registro del número de vuelos para el taller   
            aeropuertoActual.liberarPista(this);

            /* ACCEDER AEROVIA  */
            // Cambia de aeropuerto para acceder a la aerovia
            if ("Madrid".equals(aeropuertoActual.getNombre())) {
                log.escribirArchivo("El avión con id " + this.id + "(" + numPasajeros + " pasajeros)" + " accede a aerovía Madrid-Barcelona", aeropuertoActual.getNombre());
                aeropuertoAntiguo = madrid;
                aeropuertoActual = barcelona;

            } else {
                log.escribirArchivo("El avión con id " + this.id + "(" + numPasajeros + " pasajeros)" + " accede a aerovía Barcelona-Madrid", aeropuertoActual.getNombre());
                aeropuertoAntiguo = barcelona;
                aeropuertoActual = madrid;
            }
            aeropuertoAntiguo.accederAerovia(this);
            Servidor.dormir(15000, 30000);

            /* OBTENER PISTA */
            aeropuertoActual.solicitarPista(aeropuertoAntiguo, this); //Espera entre 1-5 seg hasta conseguirla
            log.escribirArchivo("El avión con id " + this.id + "(" + numPasajeros + " pasajeros)" + " ha entrado en la PISTA " + (posicionPista + 1), aeropuertoActual.getNombre());
            Servidor.dormir(1000, 5000); //Dura 1-5 seg en aterrizar
            aeropuertoActual.liberarPista(this);

            /* ACCEDER ÁREA RODAJE */
            aeropuertoActual.areaRodaje(this);
            log.escribirArchivo("El avión con id " + this.id + "(" + numPasajeros + " pasajeros)" + " entra en el ÁREA DE RODAJE", aeropuertoActual.getNombre());
            // dura entre 3-5 seg entre la pista y las puertas de embarque
            Servidor.dormir(3000, 5000);

            /* OBTENER PUERTAS DESEMBARQUE */
            aeropuertoActual.puertasDesembarque(this);
            log.escribirArchivo("El avión con id " + this.id + " entra en la puerta de EMBARQUE " + (posPuerta + 1) + " para desembarcar " + numPasajeros + " pasajeros", aeropuertoActual.getNombre());
            Servidor.dormir(1000, 5000); //Descarga de los pasajeros
            numPasajeros = 0;
            aeropuertoActual.salirPuertasEmbarque(this);
            
            /* ACCEDER ÁREA DE ESTACIONAMIENTO */
            aeropuertoActual.areaEstacionamiento(this);
            log.escribirArchivo("El avión con id " + this.id + " entra en el ÁREA DE ESTACIONAMIENTO", aeropuertoActual.getNombre());
            Servidor.dormir(1000, 5000); //Comprobaciones de los pilotos

            /* ACCEDER TALLER */
            aeropuertoActual.taller(this);
            log.escribirArchivo("El avión con id " + this.id + " entra en el TALLER", aeropuertoActual.getNombre());

            /* ENTRAR EN HANGAR O SIGUE CON EL CICLO DE VIDA */
            if (random.nextInt(2) == 0) {
                log.escribirArchivo("El avión con id " + this.id + " entra en el HANGAR a reposar", aeropuertoActual.getNombre());
                aeropuertoActual.hangar(this, true);
            }

        }
    }
    
    /**
     * Calcula el máximo de pasajeros que puede embarcar el avión
     * en un plazo de 3 intentos.
     */
    public void embarcarPasajeros() {
        boolean maxPasajeros = false;
        int intentos = 0;
        int capacidadActual = capacidad;
        while (!maxPasajeros && intentos < 3) {
            numPasajeros += aeropuertoActual.getPasajerosDisponibles(capacidadActual);
            Servidor.dormir(1000, 3000);
            if (numPasajeros < capacidadActual) {
                intentos++;
                capacidadActual -= numPasajeros;
                Servidor.dormir(1000, 5000);
            } else {
                maxPasajeros = true;
            }
        }
    }
    
    // Get y Set

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getIdAvion() {
        return id;
    }

    public void setIdAvion(String id) {
        this.id = id;
    }

    public int getNumPasajeros() {
        return numPasajeros;
    }

    public void setNumPasajeros(int numPasajeros) {
        this.numPasajeros = numPasajeros;
    }

    public int getNumVuelos() {
        return numVuelos;
    }

    public void setNumVuelos(int numVuelos) {
        this.numVuelos = numVuelos;
    }

    public int getPosicionPista() {
        return posicionPista;
    }

    public void setPosicionPista(int posicionPista) {
        this.posicionPista = posicionPista;
    }

    public int getPosPuerta() {
        return posPuerta;
    }

    public void setPosPuerta(int posPuerta) {
        this.posPuerta = posPuerta;
    }

}
