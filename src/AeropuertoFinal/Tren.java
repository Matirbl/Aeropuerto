package AeropuertoFinal;


import java.util.concurrent.CyclicBarrier;



public class Tren implements Runnable {

    private int capacidad = 3;
    private int tiempoBarrera = 40;
    private CyclicBarrier barrera;

    public Tren() {


        this.barrera = new CyclicBarrier(capacidad, new Runnable() {
            @Override
            public void run() {
            }
        });


    }


    public void run() {

    }


}