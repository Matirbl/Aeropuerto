package AeropuertoFinal;

import Utiles.Aleatorio;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PuestoAtencion {
    private Aerolinea aerolinea;
    private int maxCapacidad;
    private LinkedBlockingDeque filaPasajeros;
    private Hall hall;
    private Guardia guardia;


    public PuestoAtencion(Aerolinea aerolinea, Guardia guardia, Hall hall) {
        this.aerolinea = aerolinea;
        this.maxCapacidad = Aleatorio.intAleatorio(5, 10);
        this.filaPasajeros = new LinkedBlockingDeque(maxCapacidad);
        this.hall = hall;
        this.guardia = guardia;

    }


    public void ingresarAPuesto(Pasajero p) {
        try {
            //offer(): retorna true si ha conseguido encolar el elemento y false si no (por ejemplo, si la cola es limitada, y el elemento no cabe).
            //poll(): desencola y retorna un elemento si existe; si no existe, retorna null.
            //peek()- Devuelve un elemento desde el frente de la cola de bloqueo vinculada. Regresa nullsi la cola está vacía.

            while (!filaPasajeros.offer(p)) {
                System.out.println("El pasajero " + p.getIdPasajero() + " espera a ser llamado en el hall");
                this.hall.esperarHall(aerolinea.getIdAerolinea());
            }
            System.out.println("El pasajero " + p.getIdPasajero() + " ingresa a la fila del puesto " + aerolinea.getIdAerolinea());

            while (this.filaPasajeros.peek() != p) {
                try {
                    // Si no es el pasajero que está al frente, espera a ser notificado.
                    System.out.println("El pasajero " + p.getIdPasajero() + " espera por su turno de atención en la fila de " + aerolinea.getIdAerolinea());
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(PuestoAtencion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (Exception e) {
        }

    }


    public void realizarCheckin(Pasajero p) {
        try {
            System.out.println(p.getIdPasajero() + " realiza checkin en puesto de atención " + getAerolinea().getIdAerolinea());

            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(p.getIdPasajero() + " abandona el puesto de atención");
        this.guardia.avisarGuardia();
        this.filaPasajeros.remove(p);


        synchronized (this) {
            // Aviso a los pasajeros que esperan que pueden ser atendidos
            this.notifyAll();
        }

    }

    public int getEspaciosLibres() {
        int res = maxCapacidad;
        if (!filaPasajeros.isEmpty()) {
            res = maxCapacidad - filaPasajeros.size();
        }
        return res;
    }

    public Aerolinea getAerolinea() {
        return this.aerolinea;
    }

    public int getMaxCapacidad() {
        return maxCapacidad;
    }
}


