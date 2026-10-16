package com.tuempresa.papeleria_campus.dao;

import com.tuempresa.papeleria_campus.database.ConexionBD;
import com.tuempresa.papeleria_campus.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> buscar(String texto, String categoria,
                                 boolean soloConExistencias)
            throws SQLException {

        List<Producto> productos = new ArrayList<>();

        String sql = """
            SELECT id_producto, codigo, nombre, categoria, precio, stock
            FROM producto
            WHERE (nombre ILIKE ? OR codigo ILIKE ?)
              AND (? = 'Todas' OR categoria = ?)
              AND (? = FALSE OR stock > 0)
            ORDER BY codigo ASC
            """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            String patron = "%" + texto.trim() + "%";

            sentencia.setString(1, patron);
            sentencia.setString(2, patron);
            sentencia.setString(3, categoria);
            sentencia.setString(4, categoria);
            sentencia.setBoolean(5, soloConExistencias);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    productos.add(new Producto(
                            resultado.getInt("id_producto"),
                            resultado.getString("codigo"),
                            resultado.getString("nombre"),
                            resultado.getString("categoria"),
                            resultado.getBigDecimal("precio"),
                            resultado.getInt("stock")
                    ));
                }
            }

            return productos;

        } catch (SQLException e) {
            System.err.println(
                    "Operación: buscar productos | SQLState: "
                            + e.getSQLState()
            );

            throw e;
        }
    }

    public int insertar(Producto producto) throws SQLException {
        String sql = """
            INSERT INTO producto (codigo, nombre, categoria, precio, stock)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, producto.getCodigo());
            sentencia.setString(2, producto.getNombre());
            sentencia.setString(3, producto.getCategoria());
            sentencia.setBigDecimal(4, producto.getPrecio());
            sentencia.setInt(5, producto.getStock());

            return sentencia.executeUpdate();

        } catch (SQLException e) {
            System.err.println(
                    "Operación: insertar producto | SQLState: "
                            + e.getSQLState()
            );

            throw e;
        }
    }

    public int actualizar(Producto producto) throws SQLException {
        String sql = """
            UPDATE producto
            SET codigo = ?, nombre = ?, categoria = ?, precio = ?, stock = ?
            WHERE id_producto = ?
            """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, producto.getCodigo());
            sentencia.setString(2, producto.getNombre());
            sentencia.setString(3, producto.getCategoria());
            sentencia.setBigDecimal(4, producto.getPrecio());
            sentencia.setInt(5, producto.getStock());
            sentencia.setInt(6, producto.getIdProducto());

            return sentencia.executeUpdate();

        } catch (SQLException e) {
            System.err.println(
                    "Operación: actualizar producto | SQLState: "
                            + e.getSQLState()
            );

            throw e;
        }
    }

    public int eliminar(int idProducto) throws SQLException {
        String sql = "DELETE FROM producto WHERE id_producto = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idProducto);

            return sentencia.executeUpdate();

        } catch (SQLException e) {
            System.err.println(
                    "Operación: eliminar producto | SQLState: "
                            + e.getSQLState()
            );

            throw e;
        }
    }
}