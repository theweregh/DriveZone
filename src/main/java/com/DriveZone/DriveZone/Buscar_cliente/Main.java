package com.DriveZone.DriveZone.Buscar_cliente;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.DriveZone.DriveZone.Buscar_cliente.Vista.HTMLGenerator;


public class Main {
    public static void main(String[] args) {
        try {
            String filePath = "src/main/java/com/DriveZone/DriveZone/Buscar_cliente/Vista/ClienteView.html";

            // Generar el archivo HTML con los datos de los clientes
            HTMLGenerator.generarHTML(filePath);

            abrirHTML(filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void abrirHTML(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            Desktop.getDesktop().browse(file.toURI());
        } else {
            System.out.println("No se encontró el archivo HTML.");
        }
    }
}