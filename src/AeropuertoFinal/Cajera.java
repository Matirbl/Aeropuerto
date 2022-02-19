package AeropuertoFinal;

public class Cajera implements Runnable {

    private Caja caja;
    String idCajera;

    public Cajera(Caja caja, String idCajera) {
        this.caja = caja;
        this.idCajera = idCajera;
    }


    public void run() {
        while (true) {
            caja.quitarProducto(idCajera);
        }
    }
}
