package cl.duoc.viviendas.dao;

import cl.duoc.viviendas.model.Vivienda;
import java.util.List;

public interface ViviendaDao<T extends Vivienda> {

    /** Lista vacía si el archivo no existe o está vacío. */
    List<T> cargar() throws PersistenciaException;

    /** Reemplaza el contenido del archivo por la lista recibida. */
    void guardar(List<T> viviendas) throws PersistenciaException;
}
