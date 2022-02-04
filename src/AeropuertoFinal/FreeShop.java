package AeropuertoFinal;

import java.util.concurrent.Semaphore;
import static Utiles.Aleatorio.intAleatorio;

public class FreeShop {

    private boolean abierto;
    private Semaphore capacidad;
    private Semaphore mutex;
    private Semaphore mutexCajaA;
    private Semaphore mutexCajaB;
    private Semaphore mutexIngresar;
    private Semaphore mutexSalir;

    public FreeShop(int capacidad) {

        abierto = true;
        mutex = new Semaphore(1, true);
        this.capacidad = new Semaphore(capacidad);
        this.mutexIngresar = new Semaphore(1, true);
        this.mutexSalir = new Semaphore(1, true);
        this.mutexCajaA = new Semaphore(1, true);
        this.mutexCajaB = new Semaphore(1, true);

    }

    public void irAFreeShop(Pasajero p) {
        if (capacidad.tryAcquire()  && getAbierto()  ) {
            ingresar(p);
            comprar(p);
            salir(p);
        } else {
            System.out.println(p.getIdPasajero() + " no pudo ingresar al freeshop");
        }
    }

    public void ingresar(Pasajero p) {
        try {
            mutexIngresar.acquire();
            System.out.println(p.getIdPasajero() + " ingresó al freeshop");
            Thread.sleep(1000);
            mutexIngresar.release();
        } catch (Exception e) {
        }
    }

    public void comprar(Pasajero p) {
        int compra = intAleatorio(2, 11);
        try {
            if (compra % 2 == 0) {
                this.mutexCajaA.acquire();
                System.out.println(p.getIdPasajero() + " abonando en caja 1");
                Thread.sleep(1000);
                this.mutexCajaA.release();
            } else {
                this.mutexCajaB.acquire();
                System.out.println(p.getIdPasajero() + " abonando en caja 2");
                Thread.sleep(3000);
                this.mutexCajaB.release();
            }
        } catch (Exception e) {
        }
    }

    private void salir(Pasajero p) {
        try {
            this.mutexSalir.acquire();
            System.out.println(p.getIdPasajero() + " está saliendo del SHOPPING");
            capacidad.release();
            this.mutexSalir.release();
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
