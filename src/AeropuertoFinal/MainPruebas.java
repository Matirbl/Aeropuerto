package AeropuertoFinal;

public class MainPruebas {
    public static void main(String[] args) {

        PuestoAtencion puestoPrueba = new PuestoAtencion(1);

        System.out.println("Se creo el puesto de atención: Puesto " + puestoPrueba.getId());
        System.out.println("capacidad Disponible: " + puestoPrueba.getEspaciosLibres());

    }
}
