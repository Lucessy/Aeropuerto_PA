/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ultra;

/**
 *
 * @author sandr
 */
public class Ultra {

    public static void main(String[] args) {
        NewClass funcion = new NewClass();
        //Prueba caja blanca genérica
        System.out.println(funcion.ultraconformed(1.0, 1.0, 1.0, 45.0, 90.0, 300000000.0, 3.0, 4.0));
        //Prueba caja blanca con coordenadas en el origen
        System.out.println(funcion.ultraconformed(0.0, 0.0, 0.0, 45.0, 45.0, 300000000.0, 0.0, 0.0));
        System.out.println(int(-0.8183));
    }
}
