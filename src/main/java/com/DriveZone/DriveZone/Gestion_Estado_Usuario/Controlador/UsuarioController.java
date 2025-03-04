package com.DriveZone.DriveZone.Gestion_Estado_Usuario.Controlador;

import com.DriveZone.DriveZone.Gestion_Estado_Usuario.Modelo.UsuarioEstado;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")  // Permite llamadas desde JavaScript en el navegador
public class UsuarioController {
/*
    private final estadoUsuarioDAO usuarioDAO;

    public UsuarioController() throws SQLException {
        this.usuarioDAO = new estadoUsuarioDAO();
    }

    @GetMapping
    public List<UsuarioEstado> obtenerUsuarios() throws SQLException {
        return usuarioDAO.buscarUsuarios(""); // Retorna todos los usuarios
    }

    @GetMapping("/buscar")
    public List<UsuarioEstado> buscarUsuarios(@RequestParam String criterio) throws SQLException {
        return usuarioDAO.buscarUsuarios(criterio); // Retorna usuarios filtrados
    }

    @PostMapping("/actualizarEstado")
    public String actualizarEstado(@RequestParam int id, @RequestParam String nuevoEstado) throws SQLException {
        return usuarioDAO.actualizarEstado(id, nuevoEstado) ? "Estado actualizado" : "Error al actualizar";
    }*/
}