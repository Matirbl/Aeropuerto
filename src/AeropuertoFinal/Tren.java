package AeropuertoFinal;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Tren implements Runnable {

    private final int capacidad = 3;
    private final int tiempoBarrera = 30;
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
                System.out.println("\u001B[31m" + "Tren arrancó su recorrido" + "\u001B[0m");
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
            System.out.println("SE INTERRUMPIÓ LA BARRERA");//e.printStackTrace();
        } catch (BrokenBarrierException e) {
            System.out.println("\u001B[31m" + "Pasajero " + pasajero.getIdPasajero() + " intenta subir, pero la barrera se encuentra rota, el tren ya arrancó" + "\u001B[0m");
            //e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("\u001B[31m" + "Pasajero " + pasajero.getIdPasajero() + " intenta subir ,pero ya pasó el tiempo de espera de la barrera, el tren arranca" + "\u001B[0m");
            trenActivo = true;
           // e.printStackTrace();
        }

        //Pasajero ya está en su condition
        this.lock.lock();
        try {

            this.pasajerosEnTren--;
            if (this.pasajerosEnTren == 0) {  //Todos los pasajeros tienen asignado un conjunto de espera, ninguno está parado.
                System.out.println("\u001B[31m" + " El pasajero: " + pasajero.getIdPasajero() + " es el último hilo necesario para que arranque el tren " + "\u001B[0m");
                this.trenEnEspera.signal();
            }
            //Pasajero espera a que el tren le diga donde bajar
            System.out.println("\u001B[31m" + "El pasajero " + pasajero.getIdPasajero() + " espera para bajar en al terminal " + terminal + "\u001B[0m");
            setEsperaTerminal(terminal);
        } catch (Exception ex) {
            Logger.getLogger(Tren.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lock.unlock();
        }

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

    public void bajar(String terminal, Pasajero pasajero) {

        System.out.println("\u001B[31m" + "El pasajero " + pasajero.getIdPasajero() + " espera baja en la terminal " + terminal + "\u001B[0m");
        switch (terminal) {
            case "TerminalA": {
                cantPasajerosA.decrementAndGet();
                break;
            }

            case "TerminalB": {
                cantPasajerosB.decrementAndGet();
                break;
            }
            case "TerminalC": {
                cantPasajerosC.decrementAndGet();
                break;
            }
            default:
                System.out.println("Error");
        }
        this.cantPasajeros.decrementAndGet();
        try {
            this.lock.lock();
            this.trenEnEspera.signal();
        } finally {
            lock.unlock();
        }
    }

    public void bajarPasajeros(AtomicInteger cantidad, Condition conjuntoEspera, String terminal) {

        while (cantidad.get() != 0) {

            try {
                this.lock.lock();
                System.out.println("\u001B[31m" + " espero a que bajen los pasajeros de la terminal " + terminal + "\u001B[0m");
                conjuntoEspera.signalAll();

                //El tren se detiene esperando a que bajen todos en la terminal
                this.trenEnEspera.await();
            } catch (Exception ex) {
                Logger.getLogger(Tren.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                this.lock.unlock();
            }
        }

    }

    public void run() {


        while (true) {
            try {
                this.lock.lock();

                while (!Tren.trenActivo) {
                    System.out.println("El tren espera para salir");
                    // tren espera que llene su capacidad para arrancar
                    this.trenEnEspera.await();
                }

            } catch (InterruptedException ex) {
                Logger.getLogger(Tren.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                this.lock.unlock();
            }

            System.out.println("El tren comienza su recorrido");

            System.out.println("Tren pasa por la terminal A");
            bajarPasajeros(this.cantPasajerosA, this.esperasTerminal[0], "Terminal A");

            System.out.println("Tren pasa por la terminal B");
            bajarPasajeros(this.cantPasajerosB, this.esperasTerminal[1], "Terminal B");

            System.out.println("Tren pasa por la terminal C");
            bajarPasajeros(this.cantPasajerosC, this.esperasTerminal[2], "Terminal C");

            System.out.println("Tren finaliza el recorrido");

            try {
                System.out.println("Tren vuelve a recoger pasajeros");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Tren espera nuevos pasajeros
            this.trenActivo = false;

            this.lock.lock();
            try {
                // aviso a los pasajeros que pueden volver a subir
                this.esperaSubir.signalAll();
            } finally {
                this.lock.unlock();
            }
        }


    }


}