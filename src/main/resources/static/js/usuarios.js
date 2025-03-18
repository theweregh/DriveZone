$(document).ready(function() {
    cargarUsuarios();
});

let usuariosCargados = [];
let dataTable;

async function cargarUsuarios() {
    const request = await fetch('api/usuarios', {
        method: 'GET',
        headers: getHeaders()
    });

    usuariosCargados = await request.json();

    if ($.fn.DataTable.isDataTable('#tablaUsuarios')) {
        actualizarTabla(usuariosCargados);
    } else {
        inicializarDataTable(usuariosCargados);
    }
}

function inicializarDataTable(usuarios) {
    dataTable = $('#tablaUsuarios').DataTable({
        data: usuarios,
        columns: [
            { data: "id" },
            { data: "username" },
            { data: "nombres" },
            { data: "cedula" },
            { data: "correo" },
            { data: "direccion" },
            { data: "telefono" },
            { data: "rol" },
            { data: "estado" },
            {
                data: "id",
                render: function(data, type, row) {
                    return `
                        <button class="btn btn-success btn-editar" data-id="${data}">Modificar</button>
                        <button class="btn btn-warning" onclick="cambiarEstado(${data}, '${row.estado}')">
                            ${row.estado === 'activo' ? 'Dar de baja' : 'Activar'}
                        </button>
                    `;
                }
            }
        ],
        paging: true,
        pageLength: 10,
        lengthMenu: [5, 10, 25, 50],
        searching: false,
        info: true
    });

    // Evento para el botón "Modificar"
    $('#tablaUsuarios tbody').on('click', '.btn-editar', function () {
        let id = $(this).data('id');
        modificarUsuario(id);
    });
}

function actualizarTabla(usuarios) {
    dataTable.clear().rows.add(usuarios).draw();
}

document.getElementById('busqueda').addEventListener('input', async function () {
    let filtro = this.value.toLowerCase().trim();

    if (filtro === '') {
        actualizarTabla(usuariosCargados);
        return;
    }

    let usuariosFiltrados = usuariosCargados.filter(usuario =>
        usuario.id.toString().includes(filtro) ||
        usuario.nombres.toLowerCase().includes(filtro)
    );

    actualizarTabla(usuariosFiltrados);
    await registrarBusqueda(filtro);
});

async function registrarBusqueda(criterio) {
    await fetch('api/logs/busqueda', {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify(criterio)
    });
}

function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token
    };
}

async function cambiarEstado(id, estadoActual) {
    let nuevoEstado = estadoActual === 'activo' ? 'inactivo' : 'activo';

    if (!confirm(`¿Está seguro de cambiar el estado a ${nuevoEstado}?`)) {
        return;
    }

    await fetch(`api/usuarios/${id}/estado`, {
        method: 'PUT',
        headers: getHeaders(),
        body: JSON.stringify({ estado: nuevoEstado })
    });

    cargarUsuarios();
}

function modificarUsuario(id) {
    console.log("modificarUsuario se ejecuta con ID:", id);

    let usuario = usuariosCargados.find(u => u.id === id);
    if (!usuario) {
        console.error("Usuario no encontrado");
        return;
    }

    console.log("Usuario encontrado:", usuario);

    // Llenar el formulario con los datos actuales
    //document.getElementById("edit-id").value = usuario.id;

    document.getElementById("editUsername").value = usuario.username;
    document.getElementById("editNombres").value = usuario.nombres;
    document.getElementById("editCedula").value = usuario.cedula;
    document.getElementById("editCorreo").value = usuario.correo;
    document.getElementById("editDireccion").value = usuario.direccion;
    document.getElementById("editTelefono").value = usuario.telefono;
    document.getElementById("editRol").value = usuario.rol;
    document.getElementById("editEstado").value = usuario.estado;

    console.log("Datos cargados en el modal correctamente.");

    // Agregar evento al botón de guardar cambios
    document.getElementById("btnGuardarCambios").onclick = function () {
        guardarCambiosUsuario(id);
    };
    // Abre el modal
    var modal = new bootstrap.Modal(document.getElementById('modalEditarUsuario'));
    modal.show();
}
function guardarCambiosUsuario(id) {

    console.log("Guardando cambios para usuario ID:", id);

    // Capturar valores actualizados del formulario
    let usuarioActualizado = {
        username: document.getElementById("editUsername").value,
        nombres: document.getElementById("editNombres").value,
        cedula: document.getElementById("editCedula").value,
        correo: document.getElementById("editCorreo").value,
        direccion: document.getElementById("editDireccion").value,
        telefono: document.getElementById("editTelefono").value,
        rol: document.getElementById("editRol").value,
        estado: document.getElementById("editEstado").value
    };
    // 🔹 Verificar que no haya campos vacíos
        for (let key in usuarioActualizado) {
            if (usuarioActualizado[key] === "") {
                alert(`⚠ El campo ${key} no puede estar vacío.`);
                return;
            }
        }

        // Validar nombres
        const nombreRegex = /^[A-Za-zñÑáéíóúÁÉÍÓÚ\s]+$/;
        if (!nombreRegex.test(usuarioActualizado.nombres)) {
            alert("❌ Los nombres solo deben contener letras y espacios.");
            return;
        }

        // Validar cédula
        if (usuarioActualizado.cedula.length > 10) {
            alert("❌ La cédula debe tener un máximo de 10 caracteres.");
            return;
        }

        // Validar dirección
        const direccionRegex = /^[A-Za-z0-9\s#\-.\/]+$/;
        if (!direccionRegex.test(usuarioActualizado.direccion)) {
            alert("❌ La dirección contiene caracteres no permitidos.");
            return;
        }

        // Validar teléfono
        const telefonoRegex = /^\d+$/;
        if (!telefonoRegex.test(usuarioActualizado.telefono)) {
            alert("❌ El teléfono solo debe contener números.");
            return;
        }
        if (usuarioActualizado.telefono.length < 10) {
            alert("❌ El teléfono debe tener un mínimo de 10 caracteres.");
            return;
        }

        // Validar correo
        if (!validarCorreo(usuarioActualizado.correo)) {
            alert("⚠ El correo electrónico no tiene un formato válido. Debe contener '@' y terminar en '.com'");
            return;
        }

        // Validar correo de empresa y rol
        const correoEmpresaRegex = /^[a-zA-Z0-9._%+-]+@drivezone\.com$/;
        if (correoEmpresaRegex.test(usuarioActualizado.correo)) {
            if (!["Administrativo", "Empleado", "AsesorVentas"].includes(datos.rol)) {
                alert("❌ Si el usuario es empleado, debe especificar un rol válido: Administrativos, Empleados o Asesor de ventas.");
                return;
            }
        } else {
            usuarioActualizado.rol = "Cliente";
        }
    console.log("Datos enviados:", usuarioActualizado);

    let token = localStorage.getItem("token"); // Asegúrate de tener el token almacenado

    // Enviar datos al backend
    fetch(`api/usuarios/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": token // Incluir el token
        },
        body: JSON.stringify(usuarioActualizado)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error al actualizar usuario: " + response.statusText);
        }
        return response.text();
    })
    .then(data => {
        console.log("Respuesta del servidor:", data);
        alert("Usuario actualizado correctamente.");

        // Cerrar modal
        var modal = bootstrap.Modal.getInstance(document.getElementById('modalEditarUsuario'));
        modal.hide();

        // Actualizar lista de usuarios
        cargarUsuarios();
    })/*
    .catch(error => {
        console.error("Error en la actualización:", error);
        alert("Error al actualizar usuario.");
    });*/
}
function validarCorreo(email) {
    const regexCorreo = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[cC][oO][mM]$/;
    return regexCorreo.test(email);
}
function agregarUsuario() {
    window.location.href = "registrarAdmin.html";
}