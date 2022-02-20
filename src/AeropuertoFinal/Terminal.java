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

    public synchronized void avisarVuelos() {
        this.notifyAll();
    }

    public void irAfreeShop(Pasajero pasajero) {
        this.freeShop.comprar(pasajero, Aleatorio.intAleatorio(3, 6));
    }

    public synchronized void esperarEmbarque(int hora, int puestoEmbarque, Pasajero pasajero) {
        // El pasajero espera por su vuelo
        while (hora != Reloj.getHora()) {
            try {
                System.out.println(pasajero.getIdPasajero() + " Espera el vuelo a las " + hora + "hrs, en la terminal" + nombre + " con puesto de embarque " + puestoEmbarque);
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("El pasajero " +pasajero.getIdPasajero() + " abando el aeropuerto y toma su vuelo  ");
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuestoEmbarque() {

        return Aleatorio.intAleatorio(0, puestosEmbarque);

    }
}
