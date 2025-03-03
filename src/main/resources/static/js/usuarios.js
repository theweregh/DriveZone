/*$(document).ready(function() {
    cargarUsuarios();
    actualizarEmailDelUsuario();
});
function actualizarEmailDelUsuario(){
    document.getElementById('txt-email-usuario').outerHTML = localStorage.email;
}
let usuariosCargados = [];
let dataTable; // Instancia de DataTables

async function cargarUsuarios() {
    const request = await fetch('api/usuarios', {
        method: 'GET',
        headers: getHeaders()
    });

    usuariosCargados = await request.json();

    // 🔹 Si la tabla ya está inicializada, solo actualizamos los datos
    if ($.fn.DataTable.isDataTable('#tablaUsuarios')) {
        actualizarTabla(usuariosCargados);
    } else {
        inicializarDataTable(usuariosCargados);
    }
}

function inicializarDataTable(usuarios) {
    dataTable = $('#tablaUsuarios').DataTable({
        data: usuarios, // 🔹 Carga inicial de datos
        columns: [
            { data: "id" },
            { data: "username" },
            { data: "nombres" },
            { data: "cedula" },
            { data: "correo" },
            { data: "direccion" },
            { data: "telefono" },
            { data: "password" },
            {
                data: "id",
                render: function(data) {
                    return `<button class="btn btn-danger" onclick="eliminarUsuario(${data})">Eliminar</button>`;
                }
            }
        ],
        paging: true,
        pageLength: 10,
        lengthMenu: [5, 10, 25, 50],
        searching: false, // 🔹 Desactiva el buscador de DataTables
        info: true
    });
}

function actualizarTabla(usuarios) {
    dataTable.clear().rows.add(usuarios).draw(); // 🔹 Actualiza los datos sin romper DataTables
}

// 🔹 Filtrado en tiempo real por ID o nombre
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
function getHeaders(){
    return {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
    };
}
// 🔹 Función para eliminar usuario
async function eliminarUsuario(id) {
    if (!confirm('¿Está seguro de dar de baja el usuario?')) {
        return;
    }

    await fetch('api/usuarios/'+ id, {
        method: 'DELETE',
        headers: getHeaders()
    });

    cargarUsuarios(); // Recargar la tabla tras eliminar usuario
}*/$(document).ready(function() {
    cargarUsuarios();
    actualizarEmailDelUsuario();
});

function actualizarEmailDelUsuario(){
    document.getElementById('txt-email-usuario').outerHTML = localStorage.email;
}

let usuariosCargados = [];
let dataTable; // Instancia de DataTables

async function cargarUsuarios() {
    const request = await fetch('api/usuarios', {
        method: 'GET',
        headers: getHeaders()
    });

    usuariosCargados = await request.json();

    // 🔹 Si la tabla ya está inicializada, solo actualizamos los datos
    if ($.fn.DataTable.isDataTable('#tablaUsuarios')) {
        actualizarTabla(usuariosCargados);
    } else {
        inicializarDataTable(usuariosCargados);
    }
}

function inicializarDataTable(usuarios) {
    dataTable = $('#tablaUsuarios').DataTable({
        data: usuarios, // 🔹 Carga inicial de datos
        columns: [
            { data: "id" },
            { data: "username" },
            { data: "nombres" },
            { data: "cedula" },
            { data: "correo" },
            { data: "direccion" },
            { data: "telefono" },
            { data: "rol" }, // 🔹 Agregado rol
            { data: "estado" }, // 🔹 Agregado estado
            {
                data: "id",
                render: function(data) {
                    return `<button class="btn btn-danger" onclick="eliminarUsuario(${data})">Eliminar</button>`;
                }
            }
        ],
        paging: true,
        pageLength: 10,
        lengthMenu: [5, 10, 25, 50],
        searching: false, // 🔹 Desactiva el buscador de DataTables
        info: true
    });
}

function actualizarTabla(usuarios) {
    dataTable.clear().rows.add(usuarios).draw(); // 🔹 Actualiza los datos sin romper DataTables
}

// 🔹 Filtrado en tiempo real por ID o nombre
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

function getHeaders(){
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token
    };
}

// 🔹 Función para eliminar usuario
async function eliminarUsuario(id) {
    if (!confirm('¿Está seguro de dar de baja el usuario?')) {
        return;
    }

    await fetch('api/usuarios/'+ id, {
        method: 'DELETE',
        headers: getHeaders()
    });

    cargarUsuarios(); // Recargar la tabla tras eliminar usuario
}
