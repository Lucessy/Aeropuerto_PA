/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aeropuertos;

/**
 *
 * @author sandr
 */
public class Bus extends Thread{
    private int capacidad;
    private String id, stringNumId;
    
    public Bus(int id){
        this.capacidad=0;
        this.id = generarId(id);
    }
    
    public String generarId(int num){
        stringNumId = String.format("%04d", num+1);
        id = "B-"+stringNumId;
        System.out.print(id+"\n");
        return id;
    }
}
