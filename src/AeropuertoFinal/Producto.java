package AeropuertoFinal;

import Utiles.Aleatorio;

public class Producto {
    private String nombre;
    private int precio;

    public Producto(String nombre) {
        this.nombre = nombre;
        this.precio = Aleatorio.intAleatorio(10, 1000);
    }

    public int getPrecio() {
        return this.precio;
    }

}