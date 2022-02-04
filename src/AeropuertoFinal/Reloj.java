/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AeropuertoFinal;

import java.util.concurrent.Semaphore;
import static Utiles.Aleatorio.intAleatorio;

/**
 *
 * @author Pucheta Matías, FAI - 1648
 */
public class Reloj implements Runnable {

    private int hora;
    private Aeropuerto aero;
    private Semaphore mutex;

    public Reloj(int hora) {
        this.hora = hora;
        mutex = new Semaphore(1, true);
    }

    public void pasarTiempo() {
        try {
            mutex.acquire();
            hora = (hora + 1) % 24;
            mutex.release();
            if (hora > 6 && hora < 22) {
                Thread.sleep(3000);
            } else {
                //el tiempo pasa más rápido cuando el aeropuerto está cerrado
                Thread.sleep(1500);
            }
        } catch (Exception e) {
        }
    }

    public int getHora() {
        int salida = 0;
        try {
            mutex.acquire();
            salida = hora;
            mutex.release();
        } catch (Exception e) {
        }
        return salida;
    }

    public String obtenerHora() {
        return (hora + " hrs");
    }

    @Override
    public void run() {
        while (true) {
            System.out.println( obtenerHora());
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