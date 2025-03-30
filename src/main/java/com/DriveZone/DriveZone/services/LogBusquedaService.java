package com.DriveZone.DriveZone.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Servicio para registrar búsquedas de usuarios en un archivo de logs.
 * <p>
 * Esta clase permite registrar información sobre búsquedas realizadas
 * por los usuarios en la aplicación, almacenando la fecha, el usuario
 * y el criterio de búsqueda en un archivo de texto ubicado en `logs/busquedas.log`.
 * </p>
 *
 * <h2>Ejemplo de uso:</h2>
 * <pre>
 *     &#64;Autowired
 *     private LogBusqueda logBusqueda;
 *
 *     logBusqueda.registrarBusqueda("Juan Pérez", "Llanta deportiva 18 pulgadas");
 * </pre>
 *
 * @author DriveZone Team
 * @version 1.0
 * @since 2025-03-08
 */
@Service
public class LogBusquedaService {
    /**
     * Ruta del archivo donde se almacenan los logs de búsqueda.
     */
    private static final String LOG_FILE_PATH = "logs/busquedas.log"; // Ruta del archivo de logs
    /**
     * Formato de fecha y hora para registrar cada búsqueda.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * Logger para capturar errores en la escritura del log.
     */
    private static final Logger LOGGER = Logger.getLogger(LogBusquedaService.class.getName());

    /**
     * Registra una búsqueda realizada por un usuario en el archivo de logs.
     * <p>
     * Este método almacena la información de búsqueda en un archivo `logs/busquedas.log`.
     * Si el archivo o el directorio `logs/` no existen, se crean automáticamente.
     * </p>
     *
     * @param usuario          Nombre del usuario que realizó la búsqueda.
     * @param criterioBusqueda Término de búsqueda ingresado por el usuario.
     * @throws IOException Si ocurre un error al escribir en el archivo de logs.
     *
     *                     <h2>Ejemplo de uso:</h2>
     *                     <pre>
     *                                             logBusqueda.registrarBusqueda("Carlos López", "Filtro de aire K&N");
     *                                         </pre>
     */
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
