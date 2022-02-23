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


public class Tren {

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


        cantPasajerosA = new AtomicInteger(0);
        cantPasajerosB = new AtomicInteger(0);
        cantPasajerosC = new AtomicInteger(0);
        pasajerosEnTren = 0;

        trenActivo = false;

    }


    public void subir(Pasajero pasajero, String terminal) {
        try {

            this.lock.lock();


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

        //Pasajero entra en su condition
        this.lock.lock();
        try {

            this.pasajerosEnTren--;
            if (this.pasajerosEnTren == 0) {  //Todos los pasajeros tienen asignado un conjunto de espera, ninguno está parado.
                System.out.println("\u001B[31m" + " Los pasajeros ya tienen asignado su conjunto de espera" + "\u001B[0m");
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

    public void bajar(Pasajero pasajero, String terminal) {

        System.out.println("\u001B[31m" + "El pasajero " + pasajero.getIdPasajero() + " baja en la terminal " + terminal + "\u001B[0m");
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
        try {
            this.lock.lock();
            this.trenEnEspera.signal();
        } finally {
            lock.unlock();
        }
    }

    private void bajarPasajerosAux(AtomicInteger cantidad, Condition conjuntoEspera, String terminal) {

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

    public void bajarPasajeros(String terminal) {

        switch (terminal) {
            case "TerminalA": {
                System.out.println("Tren pasa por la terminal A");
                bajarPasajerosAux(this.cantPasajerosA, this.esperasTerminal[0], terminal);
                break;
            }
            case "TerminalB": {
                System.out.println("Tren pasa por la terminal B");
                bajarPasajerosAux(this.cantPasajerosB, this.esperasTerminal[1], terminal);
                break;
            }
            case "TerminalC": {
                System.out.println("Tren pasa por la terminal C");
                bajarPasajerosAux(this.cantPasajerosC, this.esperasTerminal[2], terminal);
                break;
            }
        }

    }

    public void iniciarTren() {
        try {
            this.lock.lock();

            while (!Tren.trenActivo) {
                System.out.println("\u001B[31m" +"El tren espera para salir"+ "\u001B[0m");
                // tren espera que llene su capacidad para arrancar
                this.trenEnEspera.await();
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Tren.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.lock.unlock();
        }

    }


    public void volverAPuesto() {
        try {
            System.out.println("\u001B[31m" +"Tren vuelve a recoger pasajeros a los puestos de atención"+ "\u001B[0m");
            Thread.sleep(4000);
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