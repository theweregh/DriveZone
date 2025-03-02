package com.DriveZone.DriveZone.Expedir_factura.Controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.DriveZone.DriveZone.Expedir_factura.DatabaseConnection.DatabaseConnectionFacturas;
import com.DriveZone.DriveZone.Expedir_factura.DatabaseConnection.DatabaseConnectionInventario;
import com.DriveZone.DriveZone.Expedir_factura.Modelo.Cliente;
import com.DriveZone.DriveZone.Expedir_factura.Modelo.Empresa;
import com.DriveZone.DriveZone.Expedir_factura.Modelo.Factura;
import com.DriveZone.DriveZone.Expedir_factura.Modelo.ItemFactura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @PostMapping("/expedirFactura")
    public ResponseEntity<Factura> expedirFactura() {
        List<ItemFactura> items = new ArrayList<>();

        try (Connection connection = DatabaseConnectionInventario.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT nombre, precio_venta, descuento, stock FROM productos");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio_venta");
                double descuento = rs.getDouble("descuento");
                int stock = rs.getInt("stock");

                if (stock > 0) {
                    items.add(new ItemFactura(nombre, 1, precio, descuento));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

        // Crear datos de Cliente y Empresa
        Cliente cliente = new Cliente(1, "Juan", "Perez", "12345678", "Calle Falsa 123", "555-1234", true);
        Empresa empresa = new Empresa("DriveZone", "900123456", "Zona Industrial");

        // Generar la factura
        Factura factura = new Factura();
        factura.setCliente(cliente);
        factura.setEmpresa(empresa);
        factura.setItems(items);
        factura.calcularTotales();

        // Guardar la factura en la base de datos
        int facturaId = guardarFacturaEnDB(factura);

        // Guardar los detalles de la factura
        if (facturaId != -1) {
            try (Connection connection = DatabaseConnectionFacturas.getConnection()) {
                for (ItemFactura item : factura.getItems()) {
                    guardarDetalleFacturaEnDB(connection, facturaId, item);
                    actualizarStockEnBD(item); // Restar del inventario
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok(factura);
    }

    public static int guardarFacturaEnDB(Factura factura) {
        int facturaId = -1; // Inicializamos con un valor no válido
        try (Connection connection = DatabaseConnectionFacturas.getConnection()) {
            String sqlFactura = "INSERT INTO facturas (empresa_nombre, nit, direccion, cliente, metodo_pago, fecha, subtotal, descuento, impuestos, total) VALUES (?, ?, ?, ?, ?, CURDATE(), ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sqlFactura, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, factura.getEmpresa().getNombre());
            pstmt.setString(2, factura.getEmpresa().getNit());
            pstmt.setString(3, factura.getEmpresa().getDireccion());
            pstmt.setString(4, factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
            pstmt.setString(5, "Efectivo");
            pstmt.setDouble(6, factura.getSubtotal());
            pstmt.setDouble(7, factura.getDescuentoTotal());
            pstmt.setDouble(8, factura.getImpuestos());
            pstmt.setDouble(9, factura.getTotal());
            pstmt.executeUpdate();

            // Obtener el ID generado
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                facturaId = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facturaId; 
    }

    private static void guardarDetalleFacturaEnDB(Connection connection, int facturaId, ItemFactura item) throws SQLException {
        String sqlDetalle = "INSERT INTO detalle_factura (factura_id, producto, cantidad, precio) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sqlDetalle);
        pstmt.setInt(1, facturaId);
        pstmt.setString(2, item.getDescripcion());
        pstmt.setInt(3, item.getCantidad());
        pstmt.setDouble(4, item.getPrecio());
        pstmt.executeUpdate();
    }

    public static void actualizarStockEnBD(ItemFactura item) {
        try (Connection connection = DatabaseConnectionInventario.getConnection()) {
            String sql = "UPDATE productos SET stock = stock - ? WHERE LOWER(nombre) = LOWER(?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, item.getCantidad());
            pstmt.setString(2, item.getDescripcion());
            
            // System.out.println("Actualizando stock: " + item.getDescripcion() + " - Cantidad: " + item.getCantidad());
            
            // int filasAfectadas = pstmt.executeUpdate();
            // System.out.println("Filas afectadas: " + filasAfectadas); // Debería ser al menos 1 si se actualiza
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public static List<ItemFactura> obtenerProductosDesdeBD(Connection connection) throws SQLException {
        List<ItemFactura> items = new ArrayList<>();
        String query = "SELECT nombre, precio_venta, descuento, stock FROM productos";
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String nombre = rs.getString("nombre");
            double precio = rs.getDouble("precio_venta");
            double descuento = rs.getDouble("descuento");
            int stock = rs.getInt("stock");

            if (stock > 0) {
                items.add(new ItemFactura(nombre, 1, precio, descuento));
            }
        }
        return items;
    }
}
