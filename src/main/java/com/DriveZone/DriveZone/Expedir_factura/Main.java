package com.DriveZone.DriveZone.Expedir_factura;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.DriveZone.DriveZone.Expedir_factura.Controlador.FacturaController;
import com.DriveZone.DriveZone.Expedir_factura.DatabaseConnection.DatabaseConnectionFacturas;
import com.DriveZone.DriveZone.Expedir_factura.DatabaseConnection.DatabaseConnectionInventario;
import com.DriveZone.DriveZone.Expedir_factura.Modelo.Cliente;
import com.DriveZone.DriveZone.Expedir_factura.Modelo.Empresa;
import com.DriveZone.DriveZone.Expedir_factura.Modelo.Factura;
import com.DriveZone.DriveZone.Expedir_factura.Modelo.ItemFactura;

import java.awt.Desktop;

public class Main {
    public static void main(String[] args) {
        try (Connection connectionInventario = DatabaseConnectionInventario.getConnection()) {
            List<ItemFactura> items = FacturaController.obtenerProductosDesdeBD(connectionInventario);
            if (items.isEmpty()) {
                System.out.println("No hay productos en stock para generar una factura.");
                return;
            }
            
            Cliente cliente = new Cliente(1, "Juan", "Perez", "12345678", "Calle Falsa 123", "555-1234", true);
            Empresa empresa = new Empresa();
            empresa.setNombre("DriveZone");
            empresa.setNit("900123456");
            empresa.setDireccion("Zona Industrial");
            
            Factura factura = new Factura();
            factura.setCliente(cliente);
            factura.setEmpresa(empresa);
            factura.setItems(items);
            factura.calcularTotales();
            
            // Guardar factura y obtener su ID
            int facturaId = FacturaController.guardarFacturaEnDB(factura);
            if (facturaId == -1) {
                System.out.println("Error al guardar la factura.");
                return;
            }
            
            // Guardar los detalles de la factura
            try (Connection connectionFacturas = DatabaseConnectionFacturas.getConnection()) {
                for (ItemFactura item : factura.getItems()) {
                    guardarDetalleFacturaEnDB(connectionFacturas, facturaId, item);
                    FacturaController.actualizarStockEnBD(item);
                }
            }
            
            System.out.println("Factura generada y almacenada en la base de datos correctamente.");
            // **Aquí abrimos el HTML**
            abrirFacturaHTML();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void guardarDetalleFacturaEnDB(Connection connection, int facturaId, ItemFactura item) throws SQLException {
        String sqlDetalle = "INSERT INTO detalle_factura (factura_id, producto, cantidad, precio) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sqlDetalle)) {
            pstmt.setInt(1, facturaId);
            pstmt.setString(2, item.getDescripcion());
            pstmt.setInt(3, item.getCantidad());
            pstmt.setDouble(4, item.getPrecio());
            pstmt.executeUpdate();
        }
    }

    private static void abrirFacturaHTML() {
        try {
            File file = new File("src/main/java/com/DriveZone/DriveZone/Expedir_factura/Vista/expedir_factura.html");
            if (file.exists()) {
                Desktop.getDesktop().browse(file.toURI());
            } else {
                System.out.println("El archivo factura.html no se encontró.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
