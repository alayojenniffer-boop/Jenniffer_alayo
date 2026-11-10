package cl.duoc.viviendas.model;

public class Departamento extends Vivienda implements ConEstacionamiento {

    private static final double COSTO_BASE = 180_000;
    private static final double RECARGO_GASTO_COMUN_ATRASADO = 0.15;

    private int numeroPiso;
    private boolean gastoComunAlDia;
    private boolean estacionamientoAsignado;

    public Departamento(String codigoPropiedad, int superficie, int numeroHabitaciones,
                        int numeroPiso, boolean gastoComunAlDia) {
        super(codigoPropiedad, superficie, numeroHabitaciones);
        setNumeroPiso(numeroPiso);
        setGastoComunAlDia(gastoComunAlDia);
        this.estacionamientoAsignado = false;
    }

    public static void validarNumeroPiso(int numeroPiso) {
        if (numeroPiso <= 0) {
            throw new IllegalArgumentException("El número de piso debe ser mayor que cero.");
        }
    }

    @Override
    public double calcularCostoArriendo() {
        double costo = COSTO_BASE;
        if (!gastoComunAlDia) {
            costo = costo * (1 + RECARGO_GASTO_COMUN_ATRASADO);
        }
        return Math.round(costo);
    }

    @Override
    public boolean tieneEstacionamientoAsignado() {
        return estacionamientoAsignado;
    }

    @Override
    public void asignarEstacionamiento() {
        this.estacionamientoAsignado = true;
    }

    public int getNumeroPiso() {
        return numeroPiso;
    }

    public void setNumeroPiso(int numeroPiso) {
        validarNumeroPiso(numeroPiso);
        this.numeroPiso = numeroPiso;
    }

    public boolean isGastoComunAlDia() {
        return gastoComunAlDia;
    }

    public void setGastoComunAlDia(boolean gastoComunAlDia) {
        this.gastoComunAlDia = gastoComunAlDia;
    }
}
