package com.DriveZone.DriveZone.Gestion_Estado_Usuario.Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/usuarios";
    private static final String USER = "root"; 
    private static final String PASSWORD = "Jesusd@1346"; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}