package AeropuertoFinal;

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
        //hacer actividades
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
