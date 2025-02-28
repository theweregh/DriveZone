package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.Usuario;

import java.util.List;

public interface UsuarioDao {
    List<Usuario> getUsers();

    void deleteUser(String id);

    void registrar(Usuario usuario);

    Usuario obtenerUsuarioPorCredenciales(Usuario usuario);
}
