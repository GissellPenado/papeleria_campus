package com.tuempresa.papeleria_campus.controller;

import com.tuempresa.papeleria_campus.dao.ProductoDAO;
import com.tuempresa.papeleria_campus.model.Producto;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;

import java.util.Locale;

public class ProductosController {

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, Integer> colId;

    @FXML
    private TableColumn<Producto, String> colCodigo;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, String> colCategoria;

    @FXML
    private TableColumn<Producto, BigDecimal> colPrecio;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    @FXML
    private Label lblEstado;

    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> cmbCategoriaFiltro;

    @FXML
    private CheckBox chkSoloExistencias;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<String> cmbCategoria;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtStock;

    @FXML
    private Label lblSeleccion;

    private Integer idSeleccionado;

    private final ProductoDAO productoDAO = new ProductoDAO();

    private final ObservableList<Producto> productos =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(dato ->
                new ReadOnlyObjectWrapper<>(dato.getValue().getIdProducto()));

        colCodigo.setCellValueFactory(dato ->
                new ReadOnlyObjectWrapper<>(dato.getValue().getCodigo()));

        colNombre.setCellValueFactory(dato ->
                new ReadOnlyObjectWrapper<>(dato.getValue().getNombre()));

        colCategoria.setCellValueFactory(dato ->
                new ReadOnlyObjectWrapper<>(dato.getValue().getCategoria()));

        colPrecio.setCellValueFactory(dato ->
                new ReadOnlyObjectWrapper<>(dato.getValue().getPrecio()));

        colStock.setCellValueFactory(dato ->
                new ReadOnlyObjectWrapper<>(dato.getValue().getStock()));

        tablaProductos.setItems(productos);
        tablaProductos.setPlaceholder(new Label("Sin productos para mostrar"));

        cmbCategoriaFiltro.getItems().setAll(
                "Todas",
                "Papelería",
                "Tecnología",
                "Accesorios"
        );

        cmbCategoriaFiltro.setValue("Todas");

        cmbCategoria.getItems().setAll(
                "Papelería",
                "Tecnología",
                "Accesorios"
        );

        tablaProductos.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        cargarFormulario(seleccionado);
                    } else {
                        limpiarCamposFormulario();
                    }
                });

        limpiarCamposFormulario();

        recargarProductos();
    }



    @FXML
    private void recargarProductos() {
        try {
            List<Producto> resultado = productoDAO.buscar(
                    txtBuscar.getText(),
                    cmbCategoriaFiltro.getValue(),
                    chkSoloExistencias.isSelected()
            );
            productos.setAll(resultado);
            lblEstado.setText("Registros visibles: " + productos.size());

            tablaProductos.setPlaceholder(new Label("Sin coincidencias"));

            if (productos.isEmpty()) {
                lblEstado.setText("Registros visibles: 0 — Sin coincidencias");
            }

        } catch (SQLException e) {
            lblEstado.setText(
                    "Error al cargar. El listado no se ha actualizado."
            );

            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de conexión");
            alerta.setHeaderText("No se pudieron cargar los productos");
            alerta.setContentText(
                    "Comprueba que PostgreSQL esté disponible "
                            + "y revisa la configuración de conexión."
            );
            alerta.showAndWait();
        }
    }

    @FXML
    private void limpiarFiltros() {
        txtBuscar.clear();
        cmbCategoriaFiltro.setValue("Todas");
        chkSoloExistencias.setSelected(false);

        recargarProductos();
    }

    private void cargarFormulario(Producto producto) {
        idSeleccionado = producto.getIdProducto();

        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        cmbCategoria.setValue(producto.getCategoria());
        txtPrecio.setText(producto.getPrecio().toPlainString());
        txtStock.setText(String.valueOf(producto.getStock()));

        lblSeleccion.setText("Producto seleccionado: ID " + idSeleccionado);
    }

    private void limpiarCamposFormulario() {
        idSeleccionado = null;

        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        txtPrecio.clear();
        txtStock.clear();

        lblSeleccion.setText("Nuevo producto");
    }

    @FXML
    private void nuevoProducto() {
        tablaProductos.getSelectionModel().clearSelection();
        limpiarCamposFormulario();
        txtCodigo.requestFocus();
    }

    private Producto validarFormulario() {
        String codigo = txtCodigo.getText().trim().toUpperCase(Locale.ROOT);
        String nombre = txtNombre.getText().trim();
        String categoria = cmbCategoria.getValue();
        String textoPrecio = txtPrecio.getText().trim();
        String textoStock = txtStock.getText().trim();

        // Mostrar los valores normalizados en el formulario.
        txtCodigo.setText(codigo);
        txtNombre.setText(nombre);

        if (codigo.isEmpty() || codigo.length() > 10) {
            throw new IllegalArgumentException(
                    "El código debe tener entre 1 y 10 caracteres."
            );
        }

        if (nombre.length() < 3 || nombre.length() > 80) {
            throw new IllegalArgumentException(
                    "El nombre debe tener entre 3 y 80 caracteres."
            );
        }

        if (categoria == null
                || !List.of("Papelería", "Tecnología", "Accesorios")
                .contains(categoria)) {
            throw new IllegalArgumentException(
                    "Selecciona una categoría válida."
            );
        }

        if (!textoPrecio.matches("-?[0-9]+(\\.[0-9]{1,2})?")) {
            throw new IllegalArgumentException(
                    "El precio debe ser un número con hasta dos decimales. "
                            + "Usa punto como separador, por ejemplo: 15.50."
            );
        }

        BigDecimal precio = new BigDecimal(textoPrecio);

        if (precio.compareTo(new BigDecimal("0.01")) < 0
                || precio.compareTo(new BigDecimal("99999999.99")) > 0) {
            throw new IllegalArgumentException(
                    "El precio debe estar entre 0.01 y 99999999.99."
            );
        }

        if (!textoStock.matches("-?[0-9]+")) {
            throw new IllegalArgumentException(
                    "El stock debe ser un número entero."
            );
        }

        int stock;

        try {
            stock = Integer.parseInt(textoStock);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El stock debe estar entre 0 y 2147483647."
            );
        }

        if (stock < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo."
            );
        }

        return new Producto(
                idSeleccionado == null ? 0 : idSeleccionado,
                codigo,
                nombre,
                categoria,
                precio,
                stock
        );
    }

    private void mostrarMensaje(Alert.AlertType tipo,
                                String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void guardarProducto() {
        if (idSeleccionado != null) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Producto seleccionado",
                    "Para crear un producto, pulsa Nuevo / Limpiar formulario. "
                            + "Para modificar el seleccionado usaremos Actualizar."
            );
            return;
        }

        try {
            Producto producto = validarFormulario();
            int filas = productoDAO.insertar(producto);

            if (filas != 1) {
                mostrarMensaje(
                        Alert.AlertType.WARNING,
                        "Guardado no confirmado",
                        "No se confirmó la inserción de un producto. "
                                + "Recarga el listado para comprobar su estado."
                );
                return;
            }

            nuevoProducto();
            recargarProductos();

            mostrarMensaje(
                    Alert.AlertType.INFORMATION,
                    "Producto guardado",
                    "El producto se guardó correctamente en PostgreSQL."
            );

        } catch (IllegalArgumentException e) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Datos inválidos",
                    e.getMessage()
            );

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                mostrarMensaje(
                        Alert.AlertType.WARNING,
                        "Código duplicado",
                        "Ya existe un producto con ese código."
                );
            } else {
                mostrarMensaje(
                        Alert.AlertType.ERROR,
                        "Error al guardar",
                        "No se pudo guardar el producto. "
                                + "Revisa la conexión y los datos ingresados."
                );
            }
        }
    }

    @FXML
    private void actualizarProducto() {
        if (idSeleccionado == null) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Selecciona un producto",
                    "Debes seleccionar una fila para actualizar."
            );
            return;
        }

        try {
            Producto producto = validarFormulario();
            int filas = productoDAO.actualizar(producto);

            if (filas == 0) {
                mostrarMensaje(
                        Alert.AlertType.WARNING,
                        "Producto no encontrado",
                        "El producto ya no existe en la base de datos. "
                                + "Recarga el listado."
                );
                return;
            }

            nuevoProducto();
            recargarProductos();

            mostrarMensaje(
                    Alert.AlertType.INFORMATION,
                    "Producto actualizado",
                    "Los cambios se guardaron correctamente en PostgreSQL."
            );

        } catch (IllegalArgumentException e) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Datos inválidos",
                    e.getMessage()
            );

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                mostrarMensaje(
                        Alert.AlertType.WARNING,
                        "Código duplicado",
                        "Ya existe otro producto con ese código."
                );
            } else {
                mostrarMensaje(
                        Alert.AlertType.ERROR,
                        "Error al actualizar",
                        "No se pudo actualizar el producto. "
                                + "Revisa la conexión y los datos ingresados."
                );
            }
        }
    }
}