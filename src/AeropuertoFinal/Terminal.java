package AeropuertoFinal;

import Utiles.Aleatorio;

public class Terminal {

    String nombre;
    private FreeShop freeShop;
    private int puestosEmbarque;

    public Terminal(String nombre, FreeShop freeShop) {
        this.nombre = nombre;
        this.freeShop = freeShop;
        this.puestosEmbarque = 3;

    }

    public synchronized void llamar() {
        this.notifyAll();
    }

    public void irAfreeShop(Pasajero pasajero) {
        this.freeShop.comprar(pasajero, Aleatorio.intAleatorio(3, 6));
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuestoEmbarque() {

        return Aleatorio.intAleatorio(0, puestosEmbarque);

    }
}
