package com.DriveZone.DriveZone.Gestion_Estado_Usuario;



import java.awt.Desktop;
import java.io.*;
import java.util.Scanner;

import com.DriveZone.DriveZone.Gestion_Estado_Usuario.Controlador.UsuarioController;
import com.DriveZone.DriveZone.Gestion_Estado_Usuario.Modelo.EstadoUsuario;
import com.DriveZone.DriveZone.Gestion_Estado_Usuario.Vista.HTMLGenerator;

import java.nio.file.*;

public class Main {
    private static final String FILE_PATH = "C:\\Users\\tostiarepa64\\Downloads\\estado.txt";
    private static final String HTML_PATH = "src/main/java/com/DriveZone/DriveZone/Gestion_Estado_Usuario/Vista/UsuarioView.html";

    public static void main(String[] args) {
        // Generar el HTML dinámicamente antes de abrirlo
        HTMLGenerator.generarHTML();
        abrirHTML(HTML_PATH);

        while (true) {
            leerArchivoEstado();
            try {
                Thread.sleep(3000); // Espera 3 segundos antes de volver a leer
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void abrirHTML(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().browse(file.toURI());
            } else {
                System.out.println("No se encontró el archivo HTML.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void leerArchivoEstado() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return; // No hacer nada si el archivo no existe

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] datos = linea.split(",");
                if (datos.length == 2) {
                    int id = Integer.parseInt(datos[0]);
                    String estadoStr = datos[1].toUpperCase();

                    try {
                        EstadoUsuario nuevoEstado = EstadoUsuario.valueOf(estadoStr);
                        boolean actualizado = UsuarioController.actualizarEstado(id, nuevoEstado);
                        System.out.println(actualizado ? "Estado actualizado." : "Error al actualizar.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Estado no válido: " + estadoStr);
                    }
                }
            }
            scanner.close();

            // Eliminar archivo después de leer
            Files.deleteIfExists(Paths.get(FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
