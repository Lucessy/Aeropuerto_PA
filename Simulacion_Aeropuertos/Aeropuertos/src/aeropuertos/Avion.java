/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author sandr
 */
public class Avion extends Thread{
    private int capacidad;
    private String id;
    private AtomicInteger capacidadAeropuerto;
    private Random random = new Random();
    private int num1, num2;
    private char letra1, letra2;
    private String stringNumId;

    //Constructor
    public Avion(String id, AtomicInteger capacidadAeropuerto){
        this.capacidad=0;
        this.id=id;
        this.capacidadAeropuerto = capacidadAeropuerto;
    }
    
    /*public void run(){
        
    }*/
    
    //Genera un id para el avión del tipo YY-XXXX, YY=> letras aleatorias entre A-Z, XXXX=> dígitos del 0001-8000 (valor pasado como argumento)
    /*public String generarId(int num){
        num1 = random.nextInt(26);
        num2 = random.nextInt(26);
        letra1 = (char)('A'+num1);
        letra2 = (char)('A'+num2);
        
        stringNumId = String.format("%04d", num+1);
        id = ""+letra1+letra2+"-"+stringNumId;
        System.out.print(id+"\n");
        return id;
    }*/ //En vez de crearse aquí lo hacer directamente en el for de HiloAux (borrar)

    @Override
    public String toString() {
        return "Avion{" + "capacidad=" + capacidad + ", id=" + id + '}';
    }
    
    
}
