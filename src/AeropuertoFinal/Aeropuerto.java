package AeropuertoFinal;

import java.util.concurrent.Semaphore;

public class Aeropuerto {
    private FreeShop freeShop;
    private Reloj reloj;
    private Semaphore mutexIngreso;
    private Semaphore mutexSalida;

    private Aeropuerto(Reloj r) {           //Constructor
        mutexIngreso = new Semaphore(1);
        mutexSalida = new Semaphore(1);
        reloj = r;
    }


    public void abrir() {
        try {
            System.out.println("********ABRE AEROPUERTO********");
           /* puedenSubirALosColes();
            carrera.setAbierto(true);
            faro.setAbierto(true);
            laguna.setAbierto(true);
            shopping.setAbierto(true);
            abrirPiletas();
            abrio.release(100);//importante que tenga correspondencia con el main*/
        } catch (Exception e) {
        }
    }

    public void ingresar(Persona p) {
        try {
            mutexIngreso.acquire();
            System.out.println(p.getIdPersona() + " INGRESA AL AEROPUERTO " + reloj.obtenerHora());
            mutexIngreso.release();
        } catch (Exception e) {
        }
    }

    public void salir(Persona p) {
        try {
            mutexSalida.acquire();
            System.err.println(p.getIdPersona() + " ABANDONA EL AEROPUERTO Y SUBE AL AVION " + reloj.obtenerHora());
            mutexSalida.release();
        } catch (Exception e) {
        }
    }


    public void cerrar() {
        try {
            /*carrera.setAbierto(false);
            faro.setAbierto(false);
            laguna.setAbierto(false);
            shopping.setAbierto(false);
            int cantPermisosRestantes = abrio.availablePermits();
            abrio.acquire(cantPermisosRestantes);
            cerrarPiletas();*/
            System.out.println("********CERRO AEROPUERTO********");


        } catch (Exception e) {
        }
    }
}
