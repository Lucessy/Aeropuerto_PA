package aeropuertos;

import java.util.Random;

public class HiloAux extends Thread {

    // Atributos
    private Log log;
    
    private final boolean esAvion;

    private final Aeropuerto madrid;
    private final Aeropuerto barcelona;

    private String stringNumId, id;
    private int num1, num2;
    private char letra1, letra2;

    private final Random random = new Random();

    // Constructor 
    public HiloAux(boolean avion, Aeropuerto madrid, Aeropuerto barcelona, Log log) {
        this.esAvion = avion;
        this.madrid = madrid;
        this.barcelona = barcelona;
        this.log = log;
    }

    // Métodos
    @Override
    public void run() {
        if (esAvion) {
            generarAviones();
            log.escribirArchivo("TODOS los aviones generados");

        } else {
            generarBuses();
            log.escribirArchivo("TODOS los buses generados");

        }
    }

    //Genera 8000 aviones con espera entre cada creación de 1-3s
    /**
     * 
     */
    public void generarAviones() {
        for (int i = 0; i < 8000; i++) {
            num1 = random.nextInt(26);
            num2 = random.nextInt(26);
            letra1 = (char) ('A' + num1);
            letra2 = (char) ('A' + num2);

            stringNumId = String.format("%04d", i + 1);
            id = "" + letra1 + letra2 + "-" + stringNumId;
            int capacidadAvion = 100 + random.nextInt(200); // Asigna un valor aleatorio entre 100 y 300 pasajeros de capacidad
            if ((i + 1) % 2 == 0) {             //Añade en el array de aviones de la clase aeropuerto los id que son pares en la instancia de madrid y los que son 
                Avion avion = new Avion(id, madrid, madrid, barcelona, capacidadAvion, log);
                avion.start();
                log.escribirArchivo("Avion " + avion.getIdAvion() + " es creado.", madrid.getNombre());
                Servidor.agregarAvion(avion);
                
            } else { //impares en la instancia de barcelona
                Avion avion = new Avion(id, barcelona, madrid, barcelona, capacidadAvion, log);
                avion.start();
                log.escribirArchivo("Avion " + avion.getIdAvion() + " es creado.", barcelona.getNombre());
                Servidor.agregarAvion(avion);
            }
            
            Servidor.dormir(1000, 3000);
        }
    }

    //Genera 4000 buses con espera entre cada creación de 0.5-1s    
    /**
     * 
     */
    public void generarBuses() {
        for (int i = 0; i < 4000; i++) {
            stringNumId = String.format("%04d", i + 1);   //Añade 0's si es necesario para el formato XXXX
            id = "B-" + stringNumId;
            if ((i + 1) % 2 == 0) {             //Añade en el array de buses de la clase aeropuerto los id que son pares en la instancia de madrid y los que son 
                Bus bus = new Bus(id, madrid, log);
                bus.start();
                log.escribirArchivo("Bus " + bus.getIdBus() + " es creado.", madrid.getNombre());
                Servidor.agregarBus(bus);
                
            } else {    //impares en la instancia de barcelona
                Bus bus = new Bus(id, barcelona, log);
                bus.start();
                log.escribirArchivo("Bus " + bus.getIdBus() + " es creado.", barcelona.getNombre());
                Servidor.agregarBus(bus);
            }
            
            Servidor.dormir(500, 1000);
        }

    }
}
