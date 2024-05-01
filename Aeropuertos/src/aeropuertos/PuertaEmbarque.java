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
//    public void accesoPuertas(boolean esEmbarque, int indice) throws InterruptedException{
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
//    
//    //Devuelve booleano dependiendo de si cumple las condiciones para el avión que ha pedido el acceso
//    public boolean hayAcceso(boolean esEmbarque,int i){
//        if (esEmbarque && puertasEmbarque[i]==false){ //El avión necesita puerta de embarque y la que busca tiene que estar disponible
//            return i>=0 && i<5;
//        }else if(!esEmbarque && puertasEmbarque[i]==false){//El avión es de desembarque y la puerta en la que busca tiene que estar disponible
//            return i>0 && i<=5;
//        }
//    }
//}  
