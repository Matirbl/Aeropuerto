package AeropuertoFinal;

import java.util.concurrent.Semaphore;
import static Utiles.Aleatorio.intAleatorio;

public class FreeShop {

    private boolean abierto;
    private Semaphore capacidad;
    private Semaphore mutex;

    public FreeShop(int capacidad) {

        abierto = true;
        mutex = new Semaphore(1, true);
        this.capacidad = new Semaphore(capacidad);
    }



    public void ingresar(Pasajero p) {
        try {
            mutex.acquire();
            System.out.println(p.getIdPasajero() + " ingresó al freeshop");
            Thread.sleep(1000);
            mutex.release();
        } catch (Exception e) {
        }
    }

    public void comprar(Pasajero p) {

    }

    private void salir(Pasajero p) {
        try {
            this.mutex.acquire();
            System.out.println(p.getIdPasajero() + " está saliendo del freeshop");
            capacidad.release();
            this.mutex.release();
            Thread.sleep(1000);
        } catch (Exception e) {
        }
    }

    public boolean getAbierto() {
        boolean salida = false;
        try {
            mutex.acquire();
            salida = abierto;
            mutex.release();
        } catch (Exception e) {
        }
        return salida;
    }

    public void setAbierto(boolean abierto) {
        try {
            mutex.acquire();
            this.abierto = abierto;
            mutex.release();
        } catch (Exception e) {
        }
    }



}
