/*$(document).ready(function() {
    cargarUsuarios();
    //actualizarEmailDelUsuario();
});

function actualizarEmailDelUsuario() {
    document.getElementById('txt-email-usuario').outerHTML = localStorage.email;
}

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
                        <button class="btn btn-success" onclick="modificarUsuario(${data})">Modificar</button>
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
}

function actualizarTabla(usuarios) {
    dataTable.clear().rows.add(usuarios).draw();
}

document.getElementById('busqueda').addEventListener('input', function () {
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
});

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
    alert(`Función para modificar el usuario con ID: ${id}`);
}

function agregarUsuario() {
    window.location.href = "registrarAdmin.html";
}*/
$(document).ready(function() {
    cargarUsuarios();
    //actualizarEmailDelUsuario();
});

function actualizarEmailDelUsuario() {
    document.getElementById('txt-email-usuario').outerHTML = localStorage.email;
}

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
                        <button class="btn btn-success" onclick="modificarUsuario(${data})">Modificar</button>
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

    // Enviar la búsqueda al backend para registrar el log
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
    alert(`Función para modificar el usuario con ID: ${id}`);
}

function agregarUsuario() {
    window.location.href = "registrarAdmin.html";
}
