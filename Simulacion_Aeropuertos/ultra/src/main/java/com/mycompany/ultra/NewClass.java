/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ultra;

/**
 *
 * @author sandr
 */
public class NewClass {
    public double ultraconformed(double focalX, double focalY, double focalZ,double thetaPointing,double phiPointing, double frequency,double x, double y) {
        double maxD = 20;
        // Pointing vector
        double nx = Math.cos(Math.toRadians(phiPointing))*Math.sin(Math.toRadians(thetaPointing));
        double ny = Math.sin(Math.toRadians(phiPointing))*Math.sin(Math.toRadians(thetaPointing));
        double nz = Math.cos(Math.toRadians(thetaPointing));
        // Lambda
        double lambda = 3.0E08/frequency;
        // Function
        double z = 0;
        // Formulation
        double d1, d10, d2, d20, dref, di, dil, dilt, zInc;
        d1 = Math.sqrt(Math.pow(focalX-x,2)+Math.pow(focalY-y,2)+Math.pow(focalZ-z,2));
        d10 = Math.sqrt(Math.pow(focalX,2)+Math.pow(focalY,2)+Math.pow(focalZ-z,2));
        d2 = maxD-(x*nx)-(y*ny)-(z*nz);
        d20 = maxD-(z*nz);
        dref = d10+d20;
        di = d1+d2-dref;
        dil = (di/lambda)-((int)(di/lambda));
        dilt = dil*lambda;
        zInc = dilt/(2*nz);
        z += zInc;
        return z;
    }
}
