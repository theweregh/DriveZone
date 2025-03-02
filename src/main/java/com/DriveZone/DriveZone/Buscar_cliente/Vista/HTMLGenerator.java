package com.DriveZone.DriveZone.Buscar_cliente.Vista;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.DriveZone.DriveZone.Buscar_cliente.Controlador.ClienteController;
import com.DriveZone.DriveZone.Buscar_cliente.Modelo.Cliente;

public class HTMLGenerator {

    public static void generarHTML(String filePath) {
        try {
            ClienteController controller = new ClienteController();
            List<Cliente> clientes = controller.buscarCliente(""); // Obtener todos los clientes

            // Crear el contenido HTML
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html lang=\"es\">\n");
            html.append("<head>\n");
            html.append("    <meta charset=\"UTF-8\">\n");
            html.append("    <title>Buscar Cliente</title>\n");
            html.append("    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\">\n");
            html.append("</head>\n");
            html.append("<body class=\"container mt-5\">\n");
            html.append("    <h2 class=\"text-center mb-4\">Buscar Cliente</h2>\n");
            html.append("    <div class=\"input-group mb-3\">\n");
            html.append("        <input type=\"text\" id=\"criterioBusqueda\" class=\"form-control\" placeholder=\"Ingrese nombre o identificación\">\n");
            html.append("        <button onclick=\"buscarCliente()\" class=\"btn btn-primary\">Buscar</button>\n");
            html.append("    </div>\n");
            html.append("    <table class=\"table table-bordered\">\n");
            html.append("        <thead class=\"table-dark\">\n");
            html.append("            <tr>\n");
            html.append("                <th>ID</th>\n");
            html.append("                <th>Nombre</th>\n");
            html.append("                <th>Apellido</th>\n");
            html.append("                <th>Identificación</th>\n");
            html.append("                <th>Dirección</th>\n");
            html.append("                <th>Teléfono</th>\n");
            html.append("                <th>Estado</th>\n");
            html.append("            </tr>\n");
            html.append("        </thead>\n");
            html.append("        <tbody id=\"tablaClientes\">\n");

            // Mostrar los clientes iniciales
            for (Cliente cliente : clientes) {
                html.append("            <tr>\n");
                html.append("                <td>").append(cliente.getId()).append("</td>\n");
                html.append("                <td>").append(cliente.getNombre()).append("</td>\n");
                html.append("                <td>").append(cliente.getApellido()).append("</td>\n");
                html.append("                <td>").append(cliente.getIdentificacion()).append("</td>\n");
                html.append("                <td>").append(cliente.getDireccion()).append("</td>\n");
                html.append("                <td>").append(cliente.getTelefono()).append("</td>\n");
                html.append("                <td>").append(cliente.isEstado() ? "Activo" : "Inactivo").append("</td>\n");
                html.append("            </tr>\n");
            }

            html.append("        </tbody>\n");
            html.append("    </table>\n");

            // Añadir el script de búsqueda
            html.append("    <script>\n");
            html.append("        // Datos de clientes simulados (pueden venir de un archivo JSON)\n");
            html.append("        let clientes = [\n");

            // Convertir la lista de clientes a un array JavaScript
            for (Cliente cliente : clientes) {
                html.append("            { id: ").append(cliente.getId())
                    .append(", nombre: \"").append(cliente.getNombre())
                    .append("\", apellido: \"").append(cliente.getApellido())
                    .append("\", identificacion: \"").append(cliente.getIdentificacion())
                    .append("\", direccion: \"").append(cliente.getDireccion())
                    .append("\", telefono: \"").append(cliente.getTelefono())
                    .append("\", estado: ").append(cliente.isEstado()).append(" },\n");
            }

            html.append("        ];\n\n");

            html.append("        function buscarCliente() {\n");
            html.append("            let criterio = document.getElementById('criterioBusqueda').value.toLowerCase();\n");
            html.append("            let tabla = document.getElementById(\"tablaClientes\");\n");
            html.append("            tabla.innerHTML = \"\"; // Limpiar tabla antes de mostrar los resultados\n\n");
            html.append("            let resultados = clientes.filter(cliente => \n");
            html.append("                cliente.nombre.toLowerCase().includes(criterio) ||\n");
            html.append("                cliente.apellido.toLowerCase().includes(criterio) ||\n");
            html.append("                cliente.identificacion.includes(criterio)\n");
            html.append("            );\n\n");
            html.append("            if (resultados.length === 0) {\n");
            html.append("                tabla.innerHTML = \"<tr><td colspan='7' class='text-center'>No se encontraron resultados</td></tr>\";\n");
            html.append("            } else {\n");
            html.append("                resultados.forEach(cliente => {\n");
            html.append("                    let fila = `<tr>\n");
            html.append("                        <td>${cliente.id}</td>\n");
            html.append("                        <td>${cliente.nombre}</td>\n");
            html.append("                        <td>${cliente.apellido}</td>\n");
            html.append("                        <td>${cliente.identificacion}</td>\n");
            html.append("                        <td>${cliente.direccion}</td>\n");
            html.append("                        <td>${cliente.telefono}</td>\n");
            html.append("                        <td>${cliente.estado ? 'Activo' : 'Inactivo'}</td>\n");
            html.append("                    </tr>`;\n");
            html.append("                    tabla.innerHTML += fila;\n");
            html.append("                });\n");
            html.append("            }\n");
            html.append("        }\n");
            html.append("    </script>\n");

            html.append("</body>\n");
            html.append("</html>\n");

            // Escribir el contenido HTML en un archivo
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(html.toString());
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}