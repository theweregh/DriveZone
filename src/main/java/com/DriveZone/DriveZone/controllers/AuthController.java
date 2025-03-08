package com.DriveZone.DriveZone.controllers;

import com.DriveZone.DriveZone.dao.UsuarioDao;
import com.DriveZone.DriveZone.models.Usuario;
import com.DriveZone.DriveZone.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de autenticación para gestionar el inicio de sesión de los usuarios.
 * Proporciona un endpoint para validar credenciales y generar un token JWT.
 */
@RestController
public class AuthController {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Autentica a un usuario con sus credenciales y genera un token JWT si son correctas.
     *
     * @param usuario Objeto que contiene el correo y la contraseña del usuario.
     * @return Un token JWT si las credenciales son válidas, de lo contrario retorna {@code "error"}.
     */
    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public String login(@RequestBody Usuario usuario) {
        Usuario usuarioLogueado = usuarioDao.obtenerUsuarioPorCredenciales(usuario);
        if (usuarioLogueado != null) {
            String tokenJwt = jwtUtil.create(String.valueOf(usuarioLogueado.getId()), usuarioLogueado.getCorreo());
            return tokenJwt;
        }
        return "error";
    }


}
