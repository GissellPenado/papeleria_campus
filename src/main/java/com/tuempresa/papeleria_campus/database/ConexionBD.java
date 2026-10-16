package com.tuempresa.papeleria_campus.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private ConexionBD() {
    }

    public static Connection conectar() throws SQLException {
        String url = System.getenv("DB_URL");
        String usuario = System.getenv("DB_USER");
        String clave = System.getenv("DB_PASSWORD");

        if (url == null || url.isBlank()
                || usuario == null || usuario.isBlank()
                || clave == null) {
            throw new SQLException(
                    "Falta configurar DB_URL, DB_USER o DB_PASSWORD.",
                    "08001"
            );
        }

        return DriverManager.getConnection(url, usuario, clave);
    }
}