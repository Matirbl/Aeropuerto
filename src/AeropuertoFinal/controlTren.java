package AeropuertoFinal;

public class controlTren implements Runnable {

    private Tren tren;


    public controlTren(Tren tren) {
        this.tren = tren;
    }


    public void run() {
        while (true) {
            tren.iniciarTren();

            try {
                System.out.println("\u001B[31m" + "TREN VIAJANDO PUESTOS------>A" + "\u001B[0m");
                Thread.sleep(2000);
                tren.bajarPasajeros("TerminalA");


                System.out.println("\u001B[31m" + "TREN VIAJANDO A------------>B" + "\u001B[0m");
                Thread.sleep(2000);
                tren.bajarPasajeros("TerminalB");


                System.out.println("\u001B[31m" + "TREN VIAJANDO B------------>C" + "\u001B[0m");
                Thread.sleep(2000);
                tren.bajarPasajeros("TerminalC");

                tren.volverAPuesto();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            System.out.println("\u001B[31m" + "Tren finaliza el recorrido" + "\u001B[0m");
        }
    }
}
