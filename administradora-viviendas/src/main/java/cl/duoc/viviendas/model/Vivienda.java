package cl.duoc.viviendas.model;

public abstract class Vivienda {

    public static final int SUPERFICIE_MINIMA = 20;
    public static final int SUPERFICIE_MAXIMA = 500;

    private String codigoPropiedad;
    private int superficie;
    private int numeroHabitaciones;

    protected Vivienda(String codigoPropiedad, int superficie, int numeroHabitaciones) {
        // El constructor delega en los setters para aplicar las validaciones.
        setCodigoPropiedad(codigoPropiedad);
        setSuperficie(superficie);
        setNumeroHabitaciones(numeroHabitaciones);
    }

    /** Cada tipo de vivienda calcula su costo de arriendo con su propia lógica. */
    public abstract double calcularCostoArriendo();

    // ---------- Reglas de validación (las usan los setters y el formulario) ----------

    public static void validarCodigoPropiedad(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "El código de propiedad no puede ser nulo ni vacío.");
        }
    }

    public static void validarSuperficie(int superficie) {
        if (superficie < SUPERFICIE_MINIMA || superficie > SUPERFICIE_MAXIMA) {
            throw new IllegalArgumentException("La superficie debe estar entre "
                    + SUPERFICIE_MINIMA + " y " + SUPERFICIE_MAXIMA + " m2.");
        }
    }

    public static void validarNumeroHabitaciones(int numeroHabitaciones) {
        if (numeroHabitaciones <= 0) {
            throw new IllegalArgumentException(
                    "El número de habitaciones debe ser mayor que cero.");
        }
    }

    // ---------- Getters y setters ----------

    public String getCodigoPropiedad() {
        return codigoPropiedad;
    }

    public void setCodigoPropiedad(String codigoPropiedad) {
        validarCodigoPropiedad(codigoPropiedad);
        this.codigoPropiedad = codigoPropiedad.trim();
    }

    public int getSuperficie() {
        return superficie;
    }

    public void setSuperficie(int superficie) {
        validarSuperficie(superficie);
        this.superficie = superficie;
    }

    public int getNumeroHabitaciones() {
        return numeroHabitaciones;
    }

    public void setNumeroHabitaciones(int numeroHabitaciones) {
        validarNumeroHabitaciones(numeroHabitaciones);
        this.numeroHabitaciones = numeroHabitaciones;
    }

    /** Nombre del tipo de vivienda (Departamento o Casa). */
    public String getTipo() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "Código: " + codigoPropiedad + " | Superficie: " + superficie;
    }
}
