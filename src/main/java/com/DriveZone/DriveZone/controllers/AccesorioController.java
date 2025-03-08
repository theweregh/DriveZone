package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.AccesorioDao;
import com.DriveZone.DriveZone.models.Accesorio;
import com.DriveZone.DriveZone.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar los accesorios en la aplicación.
 * Proporciona endpoints para obtener accesorios individuales y listar todos los accesorios disponibles.
 */
@RestController
public class AccesorioController {
    @Autowired
    private AccesorioDao accesorioDao;
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Obtiene un accesorio específico según su ID.
     *
     * @param id Identificador del accesorio a recuperar.
     * @return El accesorio correspondiente al ID proporcionado.
     */
    @RequestMapping(value = "api/accesorio/{id}")
    public Accesorio getAccesorio(@PathVariable int id) {
        Accesorio accesorio = new Accesorio("10", "steering wheel", 10, 1, 12500D, "xd.png", 0.8);
        return accesorio;
    }

    /**
     * Obtiene la lista de todos los accesorios disponibles.
     *
     * @param token Token de autorización enviado en el encabezado de la solicitud.
     * @return Lista de accesorios si la autenticación es válida, de lo contrario retorna {@code null}.
     */
    @RequestMapping(value = "api/accesorio", method = RequestMethod.GET)
    public List<Accesorio> getAccesorios(@RequestHeader(value = "Authorization") String token) {
        System.out.println("Token recibido: " + token);
        String userId = jwtUtil.getKey(token);
        System.out.println("Usuario ID extraído: " + userId);
        if (!validarToken(token)) {
            return null;
        }

        return accesorioDao.findAll();
    }

    /**
     * Valida el token de autenticación verificando si es válido y contiene un ID de usuario.
     *
     * @param token Token JWT a validar.
     * @return {@code true} si el token es válido, {@code false} en caso contrario.
     */
    private boolean validarToken(String token) {
        String userId = jwtUtil.getKey(token);
        //se puede verificar si este user esta en la db
        return userId != null;
    }
}
