package AeropuertoFinal;

public class Aeropuerto {

    private String nombre;
    private boolean aeropuertoAbierto;

    public Aeropuerto(String nombre) {
        this.nombre = nombre;
        aeropuertoAbierto = false;
    }


    public synchronized void ingresar(Pasajero p) {

        while (!aeropuertoAbierto) {
            try {
                System.out.println("Pasajero" + p.getIdPasajero() + " está esperando a que abra el aeropuerto");
                this.wait();
            } catch (InterruptedException e) {

            }
        }
        System.out.println("Pasajero" + p.getIdPasajero() + " ingresó al aeropuerto");
    }

    public synchronized void salir(Pasajero p) {

        while (!aeropuertoAbierto) {
            try {
                this.wait();
                System.out.println("Pasajero" + p.getIdPasajero() + " abandona el aeropuerto y toma su vuelo");
            } catch (InterruptedException e) {

            }
        }
    }



    public synchronized void abrir() {
        aeropuertoAbierto = true;
        this.notifyAll();
    }

    public synchronized void cerrar() {
        aeropuertoAbierto = false;

    }
}

