package cl.duoc.viviendas.repository;

import cl.duoc.viviendas.dao.PersistenciaException;
import cl.duoc.viviendas.dao.ViviendaDao;
import cl.duoc.viviendas.model.Casa;
import cl.duoc.viviendas.model.Departamento;
import cl.duoc.viviendas.model.Vivienda;
import java.util.ArrayList;
import java.util.List;

public class ViviendaRepository implements Repository<Vivienda> {

    private final ViviendaDao<Departamento> daoDepartamentos;
    private final ViviendaDao<Casa> daoCasas;
    private final List<Vivienda> viviendas = new ArrayList<>();

    public ViviendaRepository(ViviendaDao<Departamento> daoDepartamentos,
                              ViviendaDao<Casa> daoCasas) {
        this.daoDepartamentos = daoDepartamentos;
        this.daoCasas = daoCasas;
    }

    @Override
    public void cargar() throws PersistenciaException {
        List<Vivienda> todas = new ArrayList<>(daoDepartamentos.cargar());
        todas.addAll(daoCasas.cargar());
        viviendas.clear();
        viviendas.addAll(todas);
    }

    @Override
    public List<Vivienda> listar() {
        return new ArrayList<>(viviendas);
    }

    @Override
    public void agregar(Vivienda vivienda) throws PersistenciaException {
        List<Vivienda> copia = new ArrayList<>(viviendas);
        copia.add(vivienda);
        guardarArchivoDe(vivienda, copia);
        viviendas.add(vivienda);
    }

    @Override
    public void eliminar(Vivienda vivienda) throws PersistenciaException {
        List<Vivienda> copia = new ArrayList<>(viviendas);
        copia.remove(vivienda);
        guardarArchivoDe(vivienda, copia);
        viviendas.remove(vivienda);
    }

    /** Reescribe solo el archivo del tipo de la vivienda modificada. */
    private void guardarArchivoDe(Vivienda modificada, List<Vivienda> copia)
            throws PersistenciaException {
        if (modificada instanceof Departamento) {
            daoDepartamentos.guardar(filtrar(copia, Departamento.class));
        } else if (modificada instanceof Casa) {
            daoCasas.guardar(filtrar(copia, Casa.class));
        }
    }

    private static <T extends Vivienda> List<T> filtrar(List<Vivienda> lista, Class<T> clase) {
        return lista.stream().filter(clase::isInstance).map(clase::cast).toList();
    }
}
