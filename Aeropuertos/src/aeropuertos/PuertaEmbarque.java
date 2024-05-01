///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package aeropuertos;
//
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// *
// * @author sandr
// */
//public class PuertaEmbarque {
//    private final boolean[] puertasEmbarque = new boolean[6]; //La puerta 1 reservada solo para embarque, la puerta 6 para desembarque, el resto para ambas accioens
//    private ReentrantLock lockPuertas = new ReentrantLock(true);
//    private Condition embarcar = lockPuertas.newCondition();
//    private Condition desembarcar = lockPuertas.newCondition();
//    
//    public PuertaEmbarque (){
//        for (int i=0;i<6;i++){
//            puertasEmbarque[i]=false;
//        }
//    }
//    
//    public void accesoPuertas(boolean esEmbarque, int indice){
//        lockPuertas.lock();
//        try{
//            //Esperar mientras no se cumplan las condiciones de acceso
//            while(!hayAcceso(esEmbarque,indice)){
//                embarcar.await();
//            }
//            //Accede a la puerta que le corresponde
//            embarcar.signalAll();
//        }finally{
//            lockPuertas.unlock();
//        }
//    }
//}
