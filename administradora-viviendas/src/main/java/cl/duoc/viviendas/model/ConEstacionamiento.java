package cl.duoc.viviendas.model;

/**
 * Contrato del servicio de estacionamiento de la administradora.
 * Cualquier vivienda puede incorporarse al servicio implementando esta interfaz.
 */
public interface ConEstacionamiento {

    /** Consulta si la vivienda tiene un estacionamiento asignado. */
    boolean tieneEstacionamientoAsignado();

    /** Asigna un estacionamiento a la vivienda. */
    void asignarEstacionamiento();
}
