package AeropuertoFinal;

import java.util.concurrent.Semaphore;

public class Aeropuerto {
    private FreeShop freeShop;
    private int hall;
    private Reloj reloj;
    private Semaphore mutexIngreso;
    private Semaphore mutexSalida;
    private Semaphore[] guardias;
    private PuestoAtencion[] puestosAtencion;


    private Aeropuerto(Reloj r) {           //Constructor
        mutexIngreso = new Semaphore(1);
        mutexSalida = new Semaphore(1);
        reloj = r;
        hall = 500;
        for (int i = 0; i < puestosAtencion.length; i++) {
            puestosAtencion[i] = new PuestoAtencion(i);
        }
    }


    public void abrir() {
        try {
            System.out.println("********ABRE AEROPUERTO********");
            freeShop.setAbierto(true);
            /*
            carrera.setAbierto(true);
            faro.setAbierto(true);
            laguna.setAbierto(true);
            abrirPiletas();
            abrio.release(100);//importante que tenga correspondencia con el main*/
        } catch (Exception e) {
        }
    }

    public void ingresar(Persona p, int nroPuesto) {
        boolean noIngreso = true;
        try {
            mutexIngreso.acquire();
            System.out.println(p.getIdPersona() + " INGRESA AL AEROPUERTO " + reloj.obtenerHora());

            while (noIngreso) {
                if (puestosAtencion[nroPuesto].ingresarAPuesto(p)) {
                    System.out.println(p.getIdPersona() + "Ingresa al puesto de atención: " + nroPuesto);
                    noIngreso = false;
                } else {
                    System.out.println(p.getIdPersona() + "Espera en el hall");
                    mutexIngreso.release();
                    Thread.sleep(1000);
                }
            }
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
