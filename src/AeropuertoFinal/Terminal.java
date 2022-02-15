package AeropuertoFinal;

import Utiles.Aleatorio;

public class Terminal {

    String nombre;

    private int puestosEmbarque;

    public Terminal(String nombre) {
        this.nombre = nombre;

        this.puestosEmbarque = 3;

    }



    public synchronized void llamar() {
        this.notifyAll();
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuestoEmbarque() {

    return Aleatorio.intAleatorio(0, puestosEmbarque);

    }
}
