package AeropuertoFinal;

public class Vuelo {

    private final Aerolinea aerolinea;
    private final int hora;
    private Terminal terminal;
    private int puestoEmbarque;

    public Vuelo(Aerolinea aerolinea, int hora, Terminal terminal, int puestoEmbarque) {
        this.aerolinea = aerolinea;
        this.hora = hora;
        this.terminal = terminal;
        this.puestoEmbarque = puestoEmbarque;
    }


    public int getHora() {
        return hora;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public int getPuestoEmbarque() {
        return puestoEmbarque;
    }

    public Aerolinea getAerolinea() {
        return aerolinea;
    }


}
