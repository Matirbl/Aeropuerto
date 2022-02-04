package AeropuertoFinal;


import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PuestoInforme {

    private Semaphore mutexEntrada;
    private PuestoAtencion[] puestosAtencion;

    public PuestoInforme(PuestoAtencion[] puestosAtencion) {
        this.mutexEntrada = new Semaphore(1, true);
        this.puestosAtencion = puestosAtencion;
    }

    public PuestoAtencion buscarPuesto(Aerolinea aerolinea) {
        //Busco el puesto para la aerolinea deseada
        PuestoAtencion puestoAtencion = null;
        try {
            mutexEntrada.acquire();
            System.out.println(Thread.currentThread().getName() + " entra al puesto de informes");
            Thread.sleep(1000);
            puestoAtencion = compararAerolineas(aerolinea);

        } catch (InterruptedException ex) {
            Logger.getLogger(PuestoInforme.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println(Thread.currentThread().getName() + " se retira del puesto de informes");
            if (puestoAtencion != null) {
                System.out.println(Thread.currentThread().getName() + " tu puesto de atencion es " + puestoAtencion.getAerolinea().getIdAerolinea());
            }
            mutexEntrada.release();
        }
        return puestoAtencion;
    }

    //Este método se encarga de verificar si existe algún puesto de atención con el mismo id que la aerolinea del pasajero.
    private PuestoAtencion compararAerolineas(Aerolinea aerolinea) {
        PuestoAtencion resultado = null;
        boolean parar = false;
        int puestoActual = 0;
        while (puestoActual < puestosAtencion.length && !parar) {
            if (puestosAtencion[puestoActual].getAerolinea() == aerolinea) {
                resultado = puestosAtencion[puestoActual];
                parar = true;
            }
            puestoActual++;
        }
        return resultado;
    }
}

