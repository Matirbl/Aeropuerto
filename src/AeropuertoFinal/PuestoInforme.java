package AeropuertoFinal;


import Utiles.Aleatorio;

import java.util.concurrent.Semaphore;

public class PuestoInforme {

    private Semaphore mutexEntrada;
    private PuestoAtencion[] puestosAtencion;

    public PuestoInforme(PuestoAtencion[] puestosAtencion) {
        this.mutexEntrada = new Semaphore(1, true);
        this.puestosAtencion = puestosAtencion;
    }

    public PuestoAtencion buscarPuesto() {
        PuestoAtencion puestoAsignado = null;
        try {
            mutexEntrada.acquire();
            puestoAsignado = puestosAtencion[Aleatorio.intAleatorio(0, puestosAtencion.length - 1)];
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutexEntrada.release();
        }
        return puestoAsignado;
    }
}