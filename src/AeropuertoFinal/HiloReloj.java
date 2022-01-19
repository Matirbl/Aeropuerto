package AeropuertoFinal;

public class HiloReloj implements Runnable {

    private Aeropuerto aero;
    private Reloj reloj;
   // private TextoColorido color;

    public HiloReloj(Reloj r, Aeropuerto aero) {
        this.reloj = r;
        this.aero = aero;
        //color = new TextoColorido();
    }

    @Override
    public void run() {
        while (true) {
            System.out.println( reloj.obtenerHora());
            if (reloj.getHora() == 6) {
                aero.abrir();
            }
            if (reloj.getHora() == 22) {
                aero.cerrar();
            }
            reloj.pasarTiempo();
        }
    }}
