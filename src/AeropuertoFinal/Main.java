package AeropuertoFinal;

import Utiles.Aleatorio;

public class Main {

    private static Aeropuerto aeropuerto = new Aeropuerto("Viaje Bonito");
    private static final int cantPasajeros = 25, cantAerolineas = 3, cantPuestosAtencion = 3;
    private static Hall hall = new Hall(cantAerolineas);
    private static PuestoAtencion[] puestosAtencion = new PuestoAtencion[cantPuestosAtencion];
    private static Aerolinea[] aerolineas = new Aerolinea[cantAerolineas];
    private static Reloj reloj = new Reloj(0, aeropuerto);
    private static Tren tren = new Tren();
    private static Terminal[] terminales = new Terminal[3];


    public static void main(String[] args) {


        //CREO AEROLINEAS
        for (int i = 0; i < cantAerolineas; i++) {
            aerolineas[i] = new Aerolinea(i);
        }

        //CREO VUELOS


        //CREO GUARDIAS
        Guardia[] guardias = new Guardia[cantAerolineas];
        for (int i = 0; i < guardias.length; i++) {
            guardias[i] = new Guardia(aerolineas[i], hall);
        }

        //CREO HILOS GUARDIAS
        Thread hilosGuardia[] = new Thread[cantAerolineas];
        for (int i = 0; i < cantAerolineas; i++) {
            hilosGuardia[i] = new Thread(guardias[i]);
            hilosGuardia[i].start();
        }
        //CREO TERMINALES
        for (int i = 0; i < terminales.length; i++) {
            terminales[i] = new Terminal("Terminal: " + i );
        }



        //CREO PUESTOS DE ATENCION
        for (int i = 0; i < puestosAtencion.length; i++) {
            puestosAtencion[i] = new PuestoAtencion(aerolineas[i], guardias[i], hall, terminales);
        }

        //CREO PUESTOS DE INFORME
        PuestoInforme puestoInforme = new PuestoInforme(puestosAtencion);

        //CREO HILOS DE PASAJEROS
        Thread hilosPasajeros[] = new Thread[cantPasajeros];
        for (int i = 0; i < hilosPasajeros.length; i++) {
            hilosPasajeros[i] = new Thread(new Pasajero(i, aeropuerto, puestoInforme, aerolineas[i % cantAerolineas], tren,Aleatorio.intAleatorio(0,2)));
            hilosPasajeros[i].start();
        }

        //CREO HILO RELOJ
        Thread hiloReloj = new Thread(new Reloj(0, aeropuerto));
        hiloReloj.start();
    }
}
