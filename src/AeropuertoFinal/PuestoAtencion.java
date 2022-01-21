package AeropuertoFinal;

import Utiles.Aleatorio;

import java.util.PriorityQueue;


public class PuestoAtencion {
    private int idPuesto;
    private int maxCapacidad;
    private PriorityQueue pasajeros;


    public PuestoAtencion(int id) {
        idPuesto = id;
        maxCapacidad = Aleatorio.intAleatorio(15, 20);
        pasajeros = new PriorityQueue();
    }


    public boolean ingresarAPuesto(Object elemento) {
        boolean res = true;
        if (pasajeros.size() < maxCapacidad) {
            pasajeros.add(elemento);
            System.out.println("Se agregó un el elemento: " + elemento + " a la cola, tamaño: " + pasajeros.size());
        } else {
            res = false;
        }
        return res;
    }

    public int getEspaciosLibres() {
        int res = maxCapacidad;
        if (!pasajeros.isEmpty()) {
            res = maxCapacidad - pasajeros.size();
        }
        return res;
    }

    public int getId() {
        return idPuesto;
    }

    public int getMaxCapacidad() {
        return maxCapacidad;
    }
}


