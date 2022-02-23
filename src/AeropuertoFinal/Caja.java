package AeropuertoFinal;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Caja {

    private AtomicInteger filaEnCaja, precioFinal;
    private Semaphore semCaja, pagar;
    private int tamañoCinta;
    private LinkedBlockingDeque productosEnCinta;


    public Caja() {
        tamañoCinta = 3;
        this.filaEnCaja = new AtomicInteger(0);
        this.precioFinal = new AtomicInteger(0);
        this.semCaja = new Semaphore(1, true);
        this.productosEnCinta = new LinkedBlockingDeque(tamañoCinta);
        this.pagar = new Semaphore(0);

    }

    public void irACaja(Pasajero pasajero) {
        try {
            filaEnCaja.incrementAndGet();
            System.out.println("\u001B[35m Pasajero " + pasajero.getIdPasajero() + " se suma a la fila, esperando: " + filaEnCaja + "\u001B[0m");
            semCaja.acquire();
            System.out.println("\u001B[35m Pasajero " + pasajero.getIdPasajero() + " pasa por la caja " + "\u001B[0m");
            //AVISAR CAJERA
            filaEnCaja.decrementAndGet();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pagar(Pasajero pasajero) {
        try {
            pagar.acquire();
            System.out.println("----------------------\u001B[35m Pasajero " + pasajero.getIdPasajero() + " paga: " + precioFinal + " por sus productos y libera la caja" + "\u001B[0m------------");
            precioFinal.set(0);
            Thread.sleep(6000);
            semCaja.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void agregarProducto(Producto p, Pasajero pasajero) {
        try {
            System.out.println("\u001B[35m Pasajero " + pasajero.getIdPasajero() + " agrega un producto en la Cinta, hay " + productosEnCinta.size() + " productos en cinta" + "\u001B[0m");
            productosEnCinta.put(p);
            int precio = precioFinal.get() + p.getPrecio();
            precioFinal.set(precio);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void quitarProducto(String cajera) {
        try {
            if (obtenerFrente() != null && obtenerFrente().getNombre() == "UltimoProducto") {
                System.out.println("La cajera " + cajera + "habilita el pago de la caja ---------------------");
                pagar.release();
            }
            productosEnCinta.take();
            System.out.println("\u001B[35m" + cajera + " quita un producto de la cinta hay " + productosEnCinta.size() + " productos en cinta" + "\u001B[0m");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Producto obtenerFrente() {
        return (Producto) productosEnCinta.peek();
    }

    public int esperandoEnCaja() {
        return filaEnCaja.get();
    }

}