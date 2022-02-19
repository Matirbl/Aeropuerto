/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AeropuertoFinal;

import java.util.concurrent.Semaphore;


public class Reloj implements Runnable {

    private static int hora;
    private Aeropuerto aero;
    private Semaphore mutex;


    public Reloj(int hora, Aeropuerto aeropuerto) {
        this.aero = aeropuerto;
        this.hora = hora;
        mutex = new Semaphore(1, true);
    }

    public void pasarTiempo() {
        try {
            mutex.acquire();
            hora = (hora + 1) % 24;
            mutex.release();
            if (hora > 6 && hora < 22) {
                Thread.sleep(5000);
            } else {
                Thread.sleep(1500);
            }
        } catch (Exception e) {
        }
    }

    public static int getHora() {

        return hora;
    }

    public String obtenerHora() {
        return (" La hora es: " + hora + " hrs");
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("\u001B[33m" + obtenerHora() + "\u001B[0m");
            if (getHora() == 6) {
                aero.abrir();
            }
            if (getHora() == 22) {
                aero.cerrar();
            }
            pasarTiempo();
        }
    }

}