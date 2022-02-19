package AeropuertoFinal;

import java.util.concurrent.Semaphore;

public class FreeShop {

    private boolean abierto;
    private Semaphore capacidad, pagar;
    private Caja caja1, caja2;

    public FreeShop(Caja caja1, Caja caja2) {
        abierto = true;
        this.caja1 = caja1;
        this.caja2 = caja2;
        this.capacidad = new Semaphore(4, true);

    }

    public void ingresar(Pasajero p) {
        try {
            capacidad.acquire();
            System.out.println("\u001B[31m" + p.getIdPasajero() + " llega al freeShop" + "\u001B[0m");
            Thread.sleep(1000);
            System.out.println("\u001B[31m" + p.getIdPasajero() + " entró al freeshop" + "\u001B[0m");
        } catch (Exception e) {
        }
    }

    public void comprar(Pasajero pasajero, int cantProductos) {

        Caja caja = this.caja1;
        if (caja1.esperandoEnCaja() > caja2.esperandoEnCaja()) {
            caja = caja2;
        }
        caja.irACaja(pasajero);

        for (int i = 0; i < cantProductos; i++) {
            caja.agregarProducto(new Producto("Producto " + i), pasajero);

        }

        caja.pagar(pasajero);


    }

    private void salir(Pasajero p) {
        capacidad.release();
        System.out.println("\u001B[31m" + p.getIdPasajero() + " sale del freeshop" + "\u001B[0m");
    }


}
