package AeropuertoFinal;

import Utiles.Aleatorio;

public class Pasajero implements Runnable {

    private int idPasajero;
    private Aeropuerto aeropuerto;
    private Tren tren;
    private int idAerolinea;
    private int horaVuelo;



    public Pasajero(int id, Aeropuerto aeropuerto1, Tren tren1) {
        idPasajero = id;
        aeropuerto = aeropuerto1;
        tren = tren1;
        idAerolinea = Aleatorio.intAleatorio(1,4);              //Asumo que existen 3 aerolíneas
        horaVuelo= Aleatorio.intAleatorio(6,23);
    }

    @Override
    public void run() {
        aeropuerto.ingresar(this);
        //hacer actividades
        aeropuerto.salir(this);
    }

    public int getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(int idPasajero) {
        this.idPasajero = idPasajero;
    }


}
