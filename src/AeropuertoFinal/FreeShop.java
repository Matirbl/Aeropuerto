package AeropuertoFinal;

import java.util.concurrent.Semaphore;

public class FreeShop {


    private Semaphore capacidad;
    private Caja caja1, caja2;

    public FreeShop(Caja caja1, Caja caja2) {

        this.caja1 = caja1;
        this.caja2 = caja2;
        this.capacidad = new Semaphore(4, true);

    }

    public void ingresar(Pasajero p, String terminal) {
        try {
            capacidad.acquire();
            System.out.println("\u001B[35m Pasajero " + p.getIdPasajero() + " llega al freeShop de la  " + terminal + "\u001B[0m");
            Thread.sleep(500);
            System.out.println("\u001B[35m Pasajero " + p.getIdPasajero() + " entró al freeshop de la " + terminal + "\u001B[0m");
        } catch (Exception e) {
        }
    }

    public void comprar(Pasajero pasajero, int cantProductos) {

        Caja caja = this.caja1;
        System.out.println("Esperando Caja1 " + caja1.esperandoEnCaja());
        System.out.println("Esperando Caja2 " + caja2.esperandoEnCaja());
        System.out.println("HAY " + cantProductos + "PRODUCTOS");

        if (caja1.esperandoEnCaja() > caja2.esperandoEnCaja()) {
            caja = caja2;
            System.out.println("\u001B[35m Pasajero " + pasajero.getIdPasajero() + " pasa por caja2" + "\u001B[0m");
        } else {
            System.out.println("\u001B[35m Pasajero " + pasajero.getIdPasajero() + " pasa por caja1" + "\u001B[0m");
        }
        caja.irACaja(pasajero);

        int i = 0;
        while (i < cantProductos) {
            caja.agregarProducto(new Producto("Producto " + i), pasajero);
            i++;
        }
        if (i == cantProductos) {
            caja.agregarProducto(new Producto("UltimoProducto"), pasajero);
        }

        caja.pagar(pasajero);


    }

    public void salir(Pasajero p, String terminal) {
        capacidad.release();
        System.out.println("\u001B[35m Pasajero" + p.getIdPasajero() + " sale del freeshop" + terminal + "\u001B[0m");
    }


}
