package AeropuertoFinal;

import Utiles.Aleatorio;

public class Main {

    private static Aeropuerto aeropuerto = new Aeropuerto("Viaje Bonito");
    private static final int cantPasajeros = 5, cantAerolineas = 3, cantPuestosAtencion = 3;
    private static Hall hall = new Hall(cantAerolineas);
    private static PuestoAtencion[] puestosAtencion = new PuestoAtencion[cantPuestosAtencion];
    private static Aerolinea[] aerolineas = new Aerolinea[cantAerolineas];
    private static Tren tren = new Tren();
    private static Terminal[] terminales = new Terminal[3];
    private static Reloj reloj = new Reloj(0, aeropuerto, terminales);


    public static void main(String[] args) {

        cargarAerolineas();
        cargarTerminales();
        Guardia[] guardias = new Guardia[cantAerolineas];
        cargarGuardias(guardias);
        cargarPuestosAtencion(guardias);
        PuestoInforme puestoInforme = cargarPuestoInforme();
        cargarPasajeros(puestoInforme);
        cargarReloj();
        cargarTren();
    }

    private static void cargarAerolineas() {
        //CREO AEROLINEAS
        for (int i = 0; i < cantAerolineas; i++) {
            aerolineas[i] = new Aerolinea(i);
        }
    }

    private static void cargarGuardias(Guardia[] guardias) {
        //CREO GUARDIAS
        for (int i = 0; i < guardias.length; i++) {
            guardias[i] = new Guardia(aerolineas[i], hall);
        }
        //CREO HILOS GUARDIAS
        Thread hilosGuardia[] = new Thread[cantAerolineas];
        for (int i = 0; i < cantAerolineas; i++) {
            hilosGuardia[i] = new Thread(guardias[i]);
            hilosGuardia[i].start();
        }
    }

    private static void cargarTerminales() {
        //CREO CAJAS
        Caja caja1 = new Caja();
        Caja caja2 = new Caja();

        //CREO CAJERAS
        Thread cajera1 = new Thread(new Cajera(caja1, "Cajera 1"));
        Thread cajera2 = new Thread(new Cajera(caja1, "Cajera 2"));
        cajera1.start();
        cajera2.start();

        //CREO FREESHOP
        FreeShop freeShop = new FreeShop(caja1, caja2);

        //CREO TERMINALES
        String[] nombresTerminales = new String[]{"TerminalA", "TerminalB", "TerminalC"};
        for (int i = 0; i < terminales.length; i++) {
            terminales[i] = new Terminal(nombresTerminales[i], freeShop);
        }

    }

    public static void cargarPuestosAtencion(Guardia[] guardias) {
        //CREO PUESTOS DE ATENCION
        for (int i = 0; i < puestosAtencion.length; i++) {
            puestosAtencion[i] = new PuestoAtencion(aerolineas[i], guardias[i], hall, terminales);
        }
    }

    private static PuestoInforme cargarPuestoInforme() {
        //CREO PUESTOS DE INFORME
        PuestoInforme puestoInforme = new PuestoInforme(puestosAtencion);
        return puestoInforme;
    }

    private static void cargarPasajeros(PuestoInforme puestoInforme) {
        //CREO HILOS DE PASAJEROS
        Thread hilosPasajeros[] = new Thread[cantPasajeros];
        for (int i = 0; i < hilosPasajeros.length; i++) {
            hilosPasajeros[i] = new Thread(new Pasajero(i, aeropuerto, puestoInforme, aerolineas[i % cantAerolineas], tren, Aleatorio.intAleatorio(0, 2)));
            hilosPasajeros[i].start();
        }

    }

    private static void cargarReloj() {
        //CREO HILO RELOJ
        Thread hiloReloj = new Thread(new Reloj(5, aeropuerto, terminales));
        hiloReloj.start();
    }

    private static void cargarTren() {
        Thread hiloTren = new Thread(tren);
        hiloTren.start();
    }
}

