package AeropuertoFinal;

public class Persona implements Runnable {

    private int idPersona;
    private Aeropuerto aeropuerto;
    private int idAerolinea;


    public Persona(int id, Aeropuerto recursoCompartido) {
        idPersona = id;
        aeropuerto = recursoCompartido;

    }

    @Override
    public void run() {
        aeropuerto.ingresar(this, idAerolinea);
        //hacer actividades
        aeropuerto.salir(this);
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }


}
