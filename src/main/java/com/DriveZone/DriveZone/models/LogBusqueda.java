package com.DriveZone.DriveZone.models;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;
@Service
public class LogBusqueda {
    private static final String LOG_FILE_PATH = "logs/busquedas.log"; // Ruta del archivo de logs
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Logger LOGGER = Logger.getLogger(LogBusqueda.class.getName());
    public static void registrarBusqueda(String usuario, String criterioBusqueda) {
        try {
            String logMessage = String.format("[%s] Usuario: %s buscó: '%s'%n",
                    LocalDateTime.now().format(FORMATTER), usuario, criterioBusqueda);

            // Asegurar que el directorio exista
            Files.createDirectories(Paths.get("logs"));

            // Escribir en el archivo de log
            Files.write(Paths.get(LOG_FILE_PATH), logMessage.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
