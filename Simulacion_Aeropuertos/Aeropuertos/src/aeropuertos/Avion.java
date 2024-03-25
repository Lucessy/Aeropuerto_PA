/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;
import java.util.Random;

/**
 *
 * @author sandr
 */
public class Avion extends Thread{
    private int capacidad;
    private String id;
    private Random random = new Random();
    private int num1, num2;
    private char letra1, letra2;
    private String stringNumId;

    public Avion(int id){
        this.capacidad=0;
        this.id=generarId(id);
    }
    
    public String generarId(int num){
        num1 = random.nextInt(26);
        num2 = random.nextInt(26);
        letra1 = (char)('A'+num1);
        letra2 = (char)('A'+num2);
        
        stringNumId = String.format("%04d", num+1);
        id = ""+letra1+letra2+"-"+stringNumId;
        System.out.print(id+"\n");
        return id;
    }

    @Override
    public String toString() {
        return "Avion{" + "capacidad=" + capacidad + ", id=" + id + '}';
    }
    
    
}
