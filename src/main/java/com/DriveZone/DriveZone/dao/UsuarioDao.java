package com.DriveZone.DriveZone.dao;

import com.DriveZone.DriveZone.models.Usuario;

import java.util.List;

public interface UsuarioDao {
    List<Usuario> getUsers();

    void deleteUser(String id);

    void registrar(Usuario usuario);

    Usuario obtenerUsuarioPorCredenciales(Usuario usuario);
    Usuario getUserById(int id); // Nuevo método para obtener usuario por ID
    void actualizarUsuario(Usuario usuario); // Nuevo método para actualizar el usuario

    Usuario obtenerUsuarioPorCorreo(String correo);
}
