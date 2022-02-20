package AeropuertoFinal;

import Utiles.Aleatorio;

public class Pasajero implements Runnable {

    private int idPasajero;
    private Aeropuerto aeropuerto;
    private PuestoInforme puestoInforme;
    private Aerolinea aerolineaPasajero;
    private Tren tren;
    private int nroVuelo;
    private Vuelo vuelo;


    public Pasajero(int id, Aeropuerto aeropuerto1, PuestoInforme puestoInforme, Aerolinea aerolinea, Tren tren, int nroVuelo) {
        idPasajero = id;
        aeropuerto = aeropuerto1;
        this.puestoInforme = puestoInforme;
        this.aerolineaPasajero = aerolinea;
        this.tren = tren;
        this.nroVuelo = nroVuelo;
        this.vuelo = null;
    }

    @Override
    public void run() {
        aeropuerto.ingresar(this);
        PuestoAtencion puestoAtencionPasajero = puestoInforme.buscarPuesto(aerolineaPasajero, this);
        puestoAtencionPasajero.ingresarAPuesto(this);
        puestoAtencionPasajero.realizarCheckin(this);

        System.out.println( "-----------------PASAJERO "+ this.getIdPasajero() + " EJECUTA SUBIR------------------");
        tren.subir(this, vuelo.getTerminal().getNombre());
        tren.bajar(vuelo.getTerminal().getNombre(), this);


        //EL PASAJERO VERIFICA QUE TIENE MAS DE DOS HORA PARA TOMAR EL VUELO

       if (Aleatorio.intAleatorio(0, 10) % 2 == 0 && (vuelo.getHora() - Reloj.getHora() > 2)) {
            vuelo.getTerminal().irAfreeShop(this);
        }

        vuelo.getTerminal().esperarEmbarque(vuelo.getHora(), vuelo.getPuestoEmbarque(), this);

        aeropuerto.salir(this);
    }

    public int getIdPasajero() {
        return this.idPasajero;
    }

    public int getNroVuelo() {
        return this.nroVuelo;
    }

    public Vuelo getVuelo() {
        return this.vuelo;
    }

    public void asignarVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }


}
