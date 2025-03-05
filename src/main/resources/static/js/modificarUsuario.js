// Función para cargar usuarios en la tabla
function cargarUsuarios() {
    let tabla = document.getElementById("tablaUsuarios");
    tabla.innerHTML = "";
    usuariosCargados.forEach(usuario => {
        let fila = `
            <tr>
                <td>${usuario.id}</td>
                <td>${usuario.username}</td>
                <td>${usuario.nombres}</td>
                <td>${usuario.cedula}</td>
                <td>${usuario.correo}</td>
                <td>${usuario.direccion}</td>
                <td>${usuario.telefono}</td>
                <td>${usuario.rol}</td>
                <td>
                    <button class="btn btn-warning btn-sm" onclick="modificarUsuario(${usuario.id})">Editar</button>
                </td>
            </tr>
        `;
        tabla.innerHTML += fila;
    });
}

// Función para abrir el modal y cargar datos
function modificarUsuario(id) {
    console.log("Modificando usuario con ID:", id);

    let usuario = usuariosCargados.find(u => u.id === id);
    if (!usuario) {
        console.error("Usuario no encontrado");
        return;
    }

    document.getElementById("edit-id").value = usuario.id;
    document.getElementById("edit-username").value = usuario.username;
    document.getElementById("edit-nombres").value = usuario.nombres;
    document.getElementById("edit-cedula").value = usuario.cedula;
    document.getElementById("edit-correo").value = usuario.correo;
    document.getElementById("edit-direccion").value = usuario.direccion;
    document.getElementById("edit-telefono").value = usuario.telefono;
    document.getElementById("edit-rol").value = usuario.rol;

    console.log("Datos cargados en el modal correctamente.");

    // Mostrar el modal
    let modal = new bootstrap.Modal(document.getElementById("modalEditarUsuario"));
    modal.show();
    console.log("Modal abierto.");
}

// Evento para guardar cambios en el formulario
document.getElementById("formEditarUsuario").addEventListener("submit", function(event) {
    event.preventDefault();

    let id = parseInt(document.getElementById("edit-id").value);
    let usuarioIndex = usuariosCargados.findIndex(u => u.id === id);
    if (usuarioIndex === -1) {
        console.error("Usuario no encontrado en la lista.");
        return;
    }

    // Actualizar datos en la lista
    usuariosCargados[usuarioIndex] = {
        id: id,
        username: document.getElementById("edit-username").value,
        nombres: document.getElementById("edit-nombres").value,
        cedula: document.getElementById("edit-cedula").value,
        correo: document.getElementById("edit-correo").value,
        direccion: document.getElementById("edit-direccion").value,
        telefono: document.getElementById("edit-telefono").value,
        rol: document.getElementById("edit-rol").value
    };

    // Recargar la tabla
    cargarUsuarios();

    // Cerrar el modal
    let modal = bootstrap.Modal.getInstance(document.getElementById("modalEditarUsuario"));
    modal.hide();

    console.log("Cambios guardados.");
});

// Cargar los usuarios al inicio
window.onload = cargarUsuarios;