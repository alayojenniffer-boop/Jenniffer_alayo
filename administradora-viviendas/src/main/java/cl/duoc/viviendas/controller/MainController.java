package cl.duoc.viviendas.controller;

import cl.duoc.viviendas.dao.GsonViviendaDao;
import cl.duoc.viviendas.dao.PersistenciaException;
import cl.duoc.viviendas.model.Casa;
import cl.duoc.viviendas.model.Departamento;
import cl.duoc.viviendas.model.Vivienda;
import cl.duoc.viviendas.repository.Repository;
import cl.duoc.viviendas.repository.ViviendaRepository;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController {

    private static final String TIPO_DEPARTAMENTO = "Departamento";
    private static final String TIPO_CASA = "Casa";
    private static final String COLOR_EXITO = "-fx-background-color: #f2f2f2; -fx-text-fill: #1b7a1b;";
    private static final String COLOR_ERROR = "-fx-background-color: #f2f2f2; -fx-text-fill: #b00020;";

    // ---------- Componentes de la vista ----------
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtSuperficie;
    @FXML private TextField txtHabitaciones;
    @FXML private TextField txtPiso;
    @FXML private CheckBox chkGastoComun;
    @FXML private CheckBox chkEstacionamiento;
    @FXML private CheckBox chkPatio;
    @FXML private Button btnRegistrar;
    @FXML private Button btnEliminar;
    @FXML private TableView<Vivienda> tblViviendas;
    @FXML private TableColumn<Vivienda, String> colTipo;
    @FXML private TableColumn<Vivienda, String> colCodigo;
    @FXML private TableColumn<Vivienda, Number> colSuperficie;
    @FXML private TableColumn<Vivienda, Number> colHabitaciones;
    @FXML private TableColumn<Vivienda, String> colCosto;
    @FXML private Label lblMensaje;

    // ---------- Estado ----------
    private Repository<Vivienda> repositorio;
    private final ObservableList<Vivienda> viviendas = FXCollections.observableArrayList();
    private final NumberFormat formatoMoneda =
            NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CL"));

    @FXML
    public void initialize() {
        // Valores de partida
        cmbTipo.setItems(FXCollections.observableArrayList(TIPO_DEPARTAMENTO, TIPO_CASA));

        // Columnas de la tabla (el costo lo calcula el modelo)
        colTipo.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getTipo()));
        colCodigo.setCellValueFactory(
                c -> new ReadOnlyStringWrapper(c.getValue().getCodigoPropiedad()));
        colSuperficie.setCellValueFactory(
                c -> new ReadOnlyIntegerWrapper(c.getValue().getSuperficie()));
        colHabitaciones.setCellValueFactory(
                c -> new ReadOnlyIntegerWrapper(c.getValue().getNumeroHabitaciones()));
        colCosto.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                "$" + formatoMoneda.format(c.getValue().calcularCostoArriendo())));
        tblViviendas.setItems(viviendas);

        // Comportamiento dinámico desde el inicio: sin tipo, todo lo propio queda deshabilitado
        actualizarCamposSegunTipo();

        // Persistencia: el controlador solo conoce la interfaz Repository
        repositorio = new ViviendaRepository(
                new GsonViviendaDao<>(Path.of("departamentos.json"), Departamento.class),
                new GsonViviendaDao<>(Path.of("casas.json"), Casa.class));
        try {
            repositorio.cargar();
            viviendas.setAll(repositorio.listar());
        } catch (PersistenciaException e) {
            mostrarError(e.getMessage() + " La aplicación inicia sin viviendas.");
        }
    }

    // ---------- Manejadores de eventos ----------

    @FXML
    private void onTipoSeleccionado() {
        actualizarCamposSegunTipo();
    }

    @FXML
    private void onRegistrar() {
        List<String> errores = new ArrayList<>();
        String tipo = cmbTipo.getValue();

        if (tipo == null) {
            errores.add("Tipo: seleccione Departamento o Casa.");
        }

        // Datos comunes (las reglas las define el modelo)
        String codigo = txtCodigo.getText();
        validar(errores, () -> Vivienda.validarCodigoPropiedad(codigo));

        Integer superficie = leerEntero(txtSuperficie, "La superficie debe ser un número entero.",
                errores);
        if (superficie != null) {
            validar(errores, () -> Vivienda.validarSuperficie(superficie));
        }

        Integer habitaciones = leerEntero(txtHabitaciones,
                "El número de habitaciones debe ser un número entero.", errores);
        if (habitaciones != null) {
            validar(errores, () -> Vivienda.validarNumeroHabitaciones(habitaciones));
        }

        // Campos propios: solo los del tipo seleccionado
        Integer piso = null;
        if (TIPO_DEPARTAMENTO.equals(tipo)) {
            piso = leerEntero(txtPiso, "El número de piso debe ser un número entero.", errores);
            if (piso != null) {
                Integer pisoLeido = piso;
                validar(errores, () -> Departamento.validarNumeroPiso(pisoLeido));
            }
        }

        if (!errores.isEmpty()) {
            mostrarError("No se registró la vivienda. " + String.join(" ", errores));
            return; // se conservan los valores para corregirlos
        }

        try {
            Vivienda vivienda;
            if (TIPO_DEPARTAMENTO.equals(tipo)) {
                Departamento departamento = new Departamento(codigo, superficie, habitaciones,
                        piso, chkGastoComun.isSelected());
                if (chkEstacionamiento.isSelected()) {
                    departamento.asignarEstacionamiento();
                }
                vivienda = departamento;
            } else {
                vivienda = new Casa(codigo, superficie, habitaciones, chkPatio.isSelected());
            }

            repositorio.agregar(vivienda);
            viviendas.setAll(repositorio.listar());
            limpiarFormulario();
            mostrarExito("Vivienda " + vivienda.getCodigoPropiedad() + " (" + vivienda.getTipo()
                    + ") registrada correctamente.");
        } catch (IllegalArgumentException e) {
            mostrarError("No se registró la vivienda. " + e.getMessage());
        } catch (PersistenciaException e) {
            mostrarError("No se registró la vivienda. " + e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        Vivienda seleccionada = tblViviendas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Seleccione una vivienda de la tabla para eliminarla.");
            return;
        }
        try {
            repositorio.eliminar(seleccionada);
            viviendas.setAll(repositorio.listar());
            mostrarExito("Vivienda " + seleccionada.getCodigoPropiedad() + " ("
                    + seleccionada.getTipo() + ") eliminada correctamente.");
        } catch (PersistenciaException e) {
            mostrarError("No se eliminó la vivienda " + seleccionada.getCodigoPropiedad()
                    + ". " + e.getMessage());
        }
    }

    // ---------- Apoyo ----------

    /** Habilita solo los campos del tipo seleccionado y limpia los del otro tipo. */
    private void actualizarCamposSegunTipo() {
        boolean esDepartamento = TIPO_DEPARTAMENTO.equals(cmbTipo.getValue());
        boolean esCasa = TIPO_CASA.equals(cmbTipo.getValue());

        txtPiso.setDisable(!esDepartamento);
        chkGastoComun.setDisable(!esDepartamento);
        chkEstacionamiento.setDisable(!esDepartamento);
        chkPatio.setDisable(!esCasa);

        if (!esDepartamento) {
            txtPiso.clear();
            chkGastoComun.setSelected(false);
            chkEstacionamiento.setSelected(false);
        }
        if (!esCasa) {
            chkPatio.setSelected(false);
        }
    }

    private void limpiarFormulario() {
        cmbTipo.getSelectionModel().clearSelection();
        cmbTipo.setValue(null);
        txtCodigo.clear();
        txtSuperficie.clear();
        txtHabitaciones.clear();
        actualizarCamposSegunTipo();
    }

    /** Ejecuta una regla del modelo y, si la rechaza, agrega su mensaje a la lista. */
    private void validar(List<String> errores, Runnable regla) {
        try {
            regla.run();
        } catch (IllegalArgumentException e) {
            errores.add(e.getMessage());
        }
    }

    private Integer leerEntero(TextField campo, String mensajeError, List<String> errores) {
        try {
            return Integer.parseInt(campo.getText().trim());
        } catch (NumberFormatException e) {
            errores.add(mensajeError);
            return null;
        }
    }

    private void mostrarExito(String mensaje) {
        lblMensaje.setStyle(COLOR_EXITO);
        lblMensaje.setText(mensaje);
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setStyle(COLOR_ERROR);
        lblMensaje.setText(mensaje);
    }
}
