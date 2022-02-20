package AeropuertoFinal;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Tren implements Runnable {

    private final int capacidad = 3;
    private final int tiempoBarrera = 40;
    private final CyclicBarrier barrera;
    private final Lock lock;
    private static boolean trenActivo;


    private final Condition trenEnEspera;
    private final Condition esperaSubir;
    private Condition[] esperasTerminal;

    private final AtomicInteger cantPasajerosA;
    private final AtomicInteger cantPasajerosB;
    private final AtomicInteger cantPasajerosC;
    private final AtomicInteger cantPasajeros;
    private int pasajerosEnTren;

    public Tren() {

        //CyclicBarrier(int parties, Runnable barrierAction)
        //Crea una nueva CyclicBarrier que se disparará cuando el número dado de partes (subprocesos) lo estén esperando,
        //y que ejecutará la acción de barrera dada cuando se dispare la barrera, realizada por el último subproceso que ingresa a la barrera.
        this.barrera = new CyclicBarrier(capacidad, new Runnable() {
            @Override
            public void run() {
                //Los pasajeros completan el tren y lo hacen arrancar
                trenActivo = true;
            }
        });

        this.lock = new ReentrantLock();

        esperasTerminal = new Condition[3];
        for (int i = 0; i < 3; i++) {
            this.esperasTerminal[i] = lock.newCondition();
        }


        trenEnEspera = lock.newCondition();
        esperaSubir = lock.newCondition();

        cantPasajeros = new AtomicInteger(0);
        cantPasajerosA = new AtomicInteger(0);
        cantPasajerosB = new AtomicInteger(0);
        cantPasajerosC = new AtomicInteger(0);
        pasajerosEnTren = 0;

        trenActivo = false;

    }


    public void subir(Pasajero pasajero, String terminal) {
        try {

            this.lock.lock();
            cantPasajeros.incrementAndGet();

            while (trenActivo || pasajerosEnTren > capacidad) {
                System.out.println("\u001B[31m" + "El pasajero " + pasajero.getIdPasajero() + " espera para subir al tren" + "\u001B[0m");
                try {
                    this.esperaSubir.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("\u001B[31m" + "El pasajero " + pasajero.getIdPasajero() + " subió al tren y espera bajar en la terminal " + terminal + "\u001B[0m");
            this.pasajerosEnTren++;
        } finally {
            this.lock.unlock();
        }

        sumarPasajero(terminal);


        //El hilo choca con la barrera (se sube al tren)
        try {
            this.barrera.await(tiempoBarrera, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            System.out.println("\u001B[31m" + "La barrera se encuentra rota, el tren arrancó" + "\u001B[0m");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("\u001B[31m" + "Ya pasó el tiempo de espera de la barrera, el tren arranca" + "\u001B[0m");
            trenActivo = true;
            e.printStackTrace();
        }

        //Pasajero ya está en su condition
        this.lock.lock();
        this.pasajerosEnTren--;
        if (this.pasajerosEnTren == 0) {
            System.out.println("\u001B[31m" + " El pasajero: " + pasajero.getIdPasajero() + " es el último hilo necesario para que arranque el tren "+"\u001B[0m");
            this.trenEnEspera.signal();
        }
        //Pasajero espera a que el tren le diga donde bajar

        esperarLlegadaTren(terminal);


    }


    private void setEsperaTerminal(String terminal) {

        switch (terminal) {
            case "TerminalA": {
                try {
                    this.esperasTerminal[0].await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "TerminalB": {
                try {
                    this.esperasTerminal[1].await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "TerminalC": {
                try {
                    this.esperasTerminal[2].await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sumarPasajero(String terminal) {

        switch (terminal) {
            case "TerminalA": {
                this.cantPasajerosA.incrementAndGet();
                break;
            }
            case "TerminalB": {
                this.cantPasajerosB.incrementAndGet();
                break;
            }
            case "TerminalC": {
                this.cantPasajerosC.incrementAndGet();
                break;
            }

        }
    }

    private void esperarLlegadaTren(String terminal) {

        switch (terminal) {
            case "TerminalA": {
                esperasTerminal[0].signalAll();
                break;
            }

            case "TerminalB": {
                esperasTerminal[1].signalAll();
                break;
            }
            case "TerminalC": {
                esperasTerminal[2].signalAll();
                break;
            }
            default:
                System.out.println("Error");
        }

    }

    public void run() {

    }


}