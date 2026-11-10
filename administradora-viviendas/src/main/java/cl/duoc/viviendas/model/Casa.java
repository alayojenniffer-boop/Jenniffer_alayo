package cl.duoc.viviendas.model;

public class Casa extends Vivienda {

    private static final double COSTO_BASE = 250_000;
    private static final double RECARGO_PATIO = 0.10;

    private boolean tienePatio;

    public Casa(String codigoPropiedad, int superficie, int numeroHabitaciones,
                boolean tienePatio) {
        super(codigoPropiedad, superficie, numeroHabitaciones);
        setTienePatio(tienePatio);
    }

    @Override
    public double calcularCostoArriendo() {
        double costo = COSTO_BASE;
        if (tienePatio) {
            costo = costo * (1 + RECARGO_PATIO);
        }
        return Math.round(costo);
    }

    public boolean isTienePatio() {
        return tienePatio;
    }

    public void setTienePatio(boolean tienePatio) {
        this.tienePatio = tienePatio;
    }
}
