package AeropuertoFinal;

import java.util.concurrent.Semaphore;

public class Aeropuerto {

    private String nombreAerolinea;

    private boolean aeropuertoAbierto;

    public Aeropuerto(String nombre) {
        nombreAerolinea = nombre;
        aeropuertoAbierto= false;
    }


    public synchronized void ingresar(Pasajero p) {

        while (!aeropuertoAbierto) {
            try {
                System.out.println(p.getIdPasajero() + " está esperando a que abra el aeropuerto");
                this.wait();
            } catch (InterruptedException e) {

            }
        }
    }

    public synchronized void salir(Pasajero p) {

        while (!aeropuertoAbierto) {
            try {
                System.out.println(p.getIdPasajero() + " abandona el aeropuerto y toma su vuelo");
                this.wait();
            } catch (InterruptedException e) {

            }
        }
    }





    //Estos metodos no son sincronizados ya que accede solo el hilo Reloj
    public void abrir(){
        aeropuertoAbierto = true;
        this.notifyAll();
    }
    public void cerrar(){
        aeropuertoAbierto = false;

    }
}

