package aeropuertos;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Avion extends Thread {

    private Log log;
    private int capacidad;
    private String id;
    private Aeropuerto aeropuertoActual;
    private Aeropuerto aeropuertoAntiguo;
    private Aeropuerto barcelona;
    private Aeropuerto madrid;
    private int numPasajeros;
    private int tiempo;
    private Random random;
    private int posicionPista;
    private int posPuerta;
    private int numVuelos;
    private boolean embarcar;
    private Semaphore semEmbarque = new Semaphore(1);
    private Semaphore semDesembarque = new Semaphore(1);

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

        //Si está a true es que el avion tiene que embarcar, sino desembarcar. Todos los aviones comienzan teniendo que embarcar
        this.embarcar = true;
    }

    // Métodos
    public void run() {
        //Solo cuando se crea el avión
        aeropuertoActual.hangar(this, false);
        log.escribirArchivo("El avión con id " + this.id + " entra en el HANGAR", aeropuertoActual.getNombre());

        while (true) {

            /* PROCESO DE EMBARCAR  */
            aeropuertoActual.areaEstacionamiento(this, false);
            log.escribirArchivo("El avión con id " + this.id + " entra en el ÁREA DE ESTACIONAMIENTO", aeropuertoActual.getNombre());

            try {
                semEmbarque.acquire();//Solo podrá obtener una puerta de embarque si el anterior ya ha obtenido una
                aeropuertoActual.puertasEmbarque(this);
            } catch (InterruptedException ex) {
                Logger.getLogger(Avion.class.getName()).log(Level.SEVERE, null, ex);
            }
            log.escribirArchivo("El avión con id " + this.id + " entra en la puerta de EMBARQUE" + (posPuerta + 1), aeropuertoActual.getNombre());

            //EMBARCAR EL MÁXIMO Nº DE PASAJEROS
            boolean maxPasajeros = false;
            int intentos = 0;
            int capacidadActual = capacidad;
            while (!maxPasajeros && intentos < 3) {
                numPasajeros += aeropuertoActual.getPasajerosDisponibles(capacidadActual);//ESTABA PUESTO EN EL ARGUMENTO EL ATRIBUTO CAPACIDAD
                Central.dormir(1000, 3000);                                       //ENTIENDO QUE TE REFERÍAS A ESTO.
                if (numPasajeros < capacidadActual) {
                    intentos++;
                    capacidadActual = capacidadActual - numPasajeros;
                    Central.dormir(1000, 5000);
                } else {
                    maxPasajeros = true;
                }
            }
            log.escribirArchivo("El avión con id " + this.id + " ha embarcado " + numPasajeros + " pasajeros", aeropuertoActual.getNombre());
            aeropuertoActual.salirPuertasEmbarque(this);

            /* PROCESO OBTENER PISTA PARA SALIR */
            aeropuertoActual.areaRodaje(this);
            log.escribirArchivo("El avión con id " + this.id + " entra en el ÁREA DE RODAJE", aeropuertoActual.getNombre());
            Central.dormir(1000, 5000); //Comprobaciones antes de entrar a pista 1-5seg.

            aeropuertoActual.pista(this);    //Pide pista libre
            log.escribirArchivo("El avión con id " + this.id + "(" + numPasajeros + " pasajeros)" + " ha entrado en la PISTA " + (posicionPista + 1), aeropuertoActual.getNombre());
            Central.dormir(1000, 3000);//Últimas comprobaciones en pista 1-3seg.
            log.escribirArchivo("El avión con id " + this.id + "(" + numPasajeros + " pasajeros)" + " ha terminado de hacer las últimas comprobaciones", aeropuertoActual.getNombre());

            Central.dormir(1000, 5000);  //Tiempo de despegue 1-5seg.
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
            Central.dormir(15000, 30000);
            
            aeropuertoActual.solicitarPista(aeropuertoAntiguo, this); //Espera entre 1-5 seg hasta conseguirla
            log.escribirArchivo("El avión con id " + this.id + "(" + numPasajeros + " pasajeros)" + " ha entrado en la PISTA " + (posicionPista + 1), aeropuertoActual.getNombre());
            Central.dormir(1000, 5000); //Dura 1-5 seg en aterrizar
            aeropuertoActual.liberarPista(this);

            aeropuertoActual.areaRodaje(this);
            log.escribirArchivo("El avión con id " + this.id + " entra en el ÁREA DE RODAJE", aeropuertoActual.getNombre());
            // dura entre 3-5 seg entre la pista y las puertas de embarque
            Central.dormir(3000, 5000);

            try {
                semDesembarque.acquire();
                aeropuertoActual.puertasDesembarque(this);
            } catch (InterruptedException ex) {
                Logger.getLogger(Avion.class.getName()).log(Level.SEVERE, null, ex);
            }
            log.escribirArchivo("El avión con id " + this.id + " entra en la puerta de EMBARQUE" + (posPuerta + 1) + " para desembarcar " + numPasajeros + " pasajeros", aeropuertoActual.getNombre());
            Central.dormir(1000, 5000); //Descarga de los pasajeros
            numPasajeros = 0;
            aeropuertoActual.salirPuertasEmbarque(this);

            aeropuertoActual.areaEstacionamiento(this, true);
            log.escribirArchivo("El avión con id " + this.id + " entra en el ÁREA DE ESTACIONAMIENTO", aeropuertoActual.getNombre());
            Central.dormir(1000, 5000); //Comprobaciones de los pilotos

            aeropuertoActual.taller(this);
            log.escribirArchivo("El avión con id " + this.id + " entra en el TALLER", aeropuertoActual.getNombre());

            if (random.nextInt(2) == 0) {
                log.escribirArchivo("El avión con id " + this.id + " entra en el HANGAR a reposar", aeropuertoActual.getNombre());
                aeropuertoActual.hangar(this, true);
            }

        }
    }

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

    public boolean isEmbarcar() {
        return embarcar;
    }

    public void setEmbarcar(boolean embarcar) {
        this.embarcar = embarcar;
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

    public Semaphore getSemEmbarque() {
        return semEmbarque;
    }

    public void setSemEmbarque(Semaphore semEmbarque) {
        this.semEmbarque = semEmbarque;
    }

    public Semaphore getSemDesembarque() {
        return semDesembarque;
    }

    public void setSemDesembarque(Semaphore semDesembarque) {
        this.semDesembarque = semDesembarque;
    }

    

    
}
