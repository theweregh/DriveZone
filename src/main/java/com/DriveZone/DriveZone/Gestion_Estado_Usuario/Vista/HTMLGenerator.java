package com.DriveZone.DriveZone.Gestion_Estado_Usuario.Vista;


import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.List;

import com.DriveZone.DriveZone.Gestion_Estado_Usuario.Controlador.UsuarioController;
import com.DriveZone.DriveZone.Gestion_Estado_Usuario.Modelo.Usuario;

public class HTMLGenerator {
    private static final String FILE_PATH = "src/main/java/com/DriveZone/DriveZone/Gestion_Estado_Usuario/Vista/UsuarioView.html";

    public static void generarHTML() {
        List<Usuario> usuarios = UsuarioController.cargarUsuariosDesdeDB();

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html lang=\"es\">\n<head>\n");
            writer.write("<meta charset=\"UTF-8\">\n");
            writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n");
            writer.write("<title>Gestión de Usuarios</title>\n");
            writer.write("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n");
            writer.write("<script>\n" +
                         "function actualizarEstado(usuarioId) {\n" +
                         "    let estado = document.getElementById('estado-' + usuarioId).value;\n" +
                         "    let contenido = usuarioId + ',' + estado;\n" +
                         "    let blob = new Blob([contenido], { type: 'text/plain' });\n" +
                         "    let a = document.createElement('a');\n" +
                         "    a.href = URL.createObjectURL(blob);\n" +
                         "    a.download = 'estado.txt';\n" +
                         "    document.body.appendChild(a);\n" +
                         "    a.click();\n" +
                         "    document.body.removeChild(a);\n" +
                         "    alert('Estado guardado. Java lo actualizará en la base de datos.');\n" +
                         "}\n" +
                         "function normalizarTexto(texto) {\n" +
                         "    return texto.normalize('NFD').replace(/[̀-ͯ]/g, '').toLowerCase();\n" +
                         "}\n" +
                         "function filtrarUsuarios() {\n" +
                         "    let input = document.getElementById('busqueda').value;\n" +
                         "    let filtro = normalizarTexto(input);\n" +
                         "    let filas = document.querySelectorAll('tbody tr');\n" +
                         "    filas.forEach(fila => {\n" +
                         "        let nombre = normalizarTexto(fila.cells[2].innerText);\n" +
                         "        let apellido = normalizarTexto(fila.cells[3].innerText);\n" +
                         "        fila.style.display = (nombre.includes(filtro) || apellido.includes(filtro)) ? '' : 'none';\n" +
                         "    });\n" +
                         "}\n" +
                         "</script>\n");
            writer.write("</head><body>\n");
            writer.write("<div class=\"container\">\n<h2>Gestión de Usuarios</h2>\n");
            writer.write("<div class=\"mb-3\">\n<input type=\"text\" class=\"form-control\" id=\"busqueda\" placeholder=\"Ingrese nombre o apellido\" onkeyup=\"filtrarUsuarios()\">\n</div>\n");
            writer.write("<div class=\"table-container\">\n<table class=\"table table-striped table-bordered\">\n<thead class=\"table-dark\">\n<tr>\n");
            writer.write("<th>ID</th><th>Nombre de Usuario</th><th>Nombres</th><th>Apellidos</th><th>Cédula</th><th>Correo</th><th>Dirección</th><th>Teléfono</th><th>Estado</th><th>Acción</th>\n</tr>\n</thead>\n<tbody>\n");
            
            for (Usuario usuario : usuarios) {
                writer.write("<tr>\n");
                writer.write("<td>" + usuario.getId() + "</td>\n");
                writer.write("<td>" + usuario.getNombreUsuario() + "</td>\n");
                writer.write("<td>" + usuario.getNombres() + "</td>\n");
                writer.write("<td>" + usuario.getApellidos() + "</td>\n");
                writer.write("<td>" + usuario.getCedula() + "</td>\n");
                writer.write("<td>" + usuario.getCorreo() + "</td>\n");
                writer.write("<td>" + usuario.getDireccion() + "</td>\n");
                writer.write("<td>" + usuario.getTelefono() + "</td>\n");
                writer.write("<td><select id='estado-" + usuario.getId() + "' class='form-select'>\n");
                for (String estado : new String[]{"ACTIVO", "INACTIVO", "FUERA_DE_SERVICIO"}) {
                    writer.write("<option value='" + estado + "' " + (usuario.getEstado().toString().equals(estado) ? "selected" : "") + ">" + estado + "</option>\n");
                }
                writer.write("</select></td>\n");
                writer.write("<td><button class='btn btn-update' onclick='actualizarEstado(" + usuario.getId() + ")'>Actualizar</button></td>\n");
                writer.write("</tr>\n");
            }
            
            writer.write("</tbody>\n</table>\n</div>\n</div>\n</body>\n</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}