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
    private Vuelo[] vuelos = new Vuelo[3];                          //existen 3 vuelos por aerolinea
    private Terminal[] terminales;

    public PuestoAtencion(Aerolinea aerolinea, Guardia guardia, Hall hall, Terminal[] terminales) {
        this.aerolinea = aerolinea;
        this.maxCapacidad = 2;
        this.filaPasajeros = new LinkedBlockingDeque(maxCapacidad);
        this.hall = hall;
        this.guardia = guardia;
        this.terminales = terminales;

        for (int i = 0; i < vuelos.length; i++) {
            Terminal terminalAsignada = terminales[Aleatorio.intAleatorio(0, terminales.length - 1)];
            vuelos[i] = new Vuelo(aerolinea, Aleatorio.intAleatorio(6, 20), terminalAsignada, terminalAsignada.getPuestoEmbarque());
        }

    }

    public void ingresarAPuesto(Pasajero p) {
        try {
            //offer(): retorna true si ha conseguido encolar el elemento y false si no (por ejemplo, si la cola es limitada, y el elemento no cabe).
            //poll(): desencola y retorna un elemento si existe; si no existe, retorna null.
            //peek()- Devuelve un elemento desde el frente de la cola de bloqueo vinculada. Regresa null si la cola está vacía.

            while (!filaPasajeros.offer(p)) {
                System.out.println("\u001B[32m" + "El pasajero " + p.getIdPasajero() + " espera a ser llamado en el hall" + "\u001B[0m");
                this.hall.esperarHall(aerolinea.getIdAerolinea());
            }

            System.out.println("\u001B[36m" + "El pasajero " + p.getIdPasajero() + " ingresa a la fila del puesto " + aerolinea.getIdAerolinea() + "\u001B[0m");

            while (this.filaPasajeros.peek() != p) {
                try {
                    // Si no es el pasajero que está al frente, espera a ser notificado.
                    System.out.println("\u001B[36m" + "El pasajero " + p.getIdPasajero() + " espera por su turno de atención en la fila de " + aerolinea.getIdAerolinea() + "\u001B[0m");
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
            System.out.println(p.getIdPasajero() + " está realizando el checkin en puesto de atención " + getAerolinea().getIdAerolinea());

            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(p.getIdPasajero() + " abandona el puesto de atención");

        this.guardia.avisarGuardia(p);
        this.filaPasajeros.remove(p);


        synchronized (this) {
            // Aviso a los pasajeros que esperan en el puesto que pueden ser atendidos
            this.notifyAll();
        }

        p.asignarVuelo(vuelos[p.getNroVuelo()]);

        System.out.println("/////////////////Al pasajero: " + p.getIdPasajero() + " se le asigna: " + p.getVuelo().getTerminal().getNombre() + " con embarque " + p.getVuelo().getPuestoEmbarque());
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


