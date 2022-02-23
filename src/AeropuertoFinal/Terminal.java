package AeropuertoFinal;

import Utiles.Aleatorio;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Terminal {

    private String nombre;
    private FreeShop freeShop;
    private int puestosEmbarque;

    public Terminal(String nombre, FreeShop freeShop) {
        this.nombre = nombre;
        this.freeShop = freeShop;
        this.puestosEmbarque = 3;

    }

    //Reloj avisa a los pasajeros que paso una hora
    public synchronized void avisarVuelos() {
        this.notifyAll();
    }

    public void irAfreeShop(Pasajero pasajero) {
        this.freeShop.ingresar(pasajero, nombre);
        this.freeShop.comprar(pasajero, Aleatorio.intAleatorio(3, 6));
        this.freeShop.salir(pasajero, nombre);
    }

    public synchronized void esperarVuelo(int hora, int puestoEmbarque, Pasajero pasajero) {
        // El pasajero es despertado por el reloj y verifica si es la hora de su vuelo
        while (hora > Reloj.getHora()) {
            try {
                System.out.println(pasajero.getIdPasajero() + " Espera el vuelo a las " + hora + "hrs, en la terminal" + nombre + " con puesto de embarque " + puestoEmbarque);
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (hora == Reloj.getHora()) {
            System.out.println("\u001B[32m" + "El pasajero " + pasajero.getIdPasajero() + " abandona el aeropuerto y toma su vuelo  " + "\u001B[0m");
        } else {
            System.out.println("\u001B[32m" + "El pasajero " + pasajero.getIdPasajero() + " llegó tarde y perdió su vuelo  " + "\u001B[0m");
        }
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuestoEmbarque() {

        return Aleatorio.intAleatorio(0, puestosEmbarque);

    }
}
