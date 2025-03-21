// Cargar el header desde header.html
        document.addEventListener("DOMContentLoaded", function () {
    fetch("header.html")
        .then(response => response.text())
        .then(data => {
            document.getElementById("header").innerHTML = data;
        })
        .catch(error => console.error("Error cargando el header:", error));
});
function cerrarSesion() {
        localStorage.removeItem("token"); // Borra el token
        localStorage.removeItem("carrito"); // Borra el carrito
        sessionStorage.clear(); // Limpia la sesión
        window.location.href = "index.html"; // Redirige al login
    }
document.addEventListener("DOMContentLoaded", cargarClientes);

function cargarClientes() {
    fetch("/clientes")
        .then(response => response.json())
        .then(clientes => {
            const tbody = document.getElementById("clientesTableBody");
            tbody.innerHTML = "";
            clientes.forEach(cliente => {
                const fila = document.createElement("tr");
                fila.setAttribute("data-id", cliente.idCliente);
                fila.innerHTML = `
                    <td>${cliente.idCliente}</td>
                    <td>${cliente.nombre}</td>
                    <td>${cliente.apellido}</td>
                    <td>${cliente.cedula}</td>
                    <td>${cliente.direccion}</td>
                    <td>${cliente.telefono}</td>
                    <td class="estado ${cliente.estado.toLowerCase()}">
                        ${cliente.estado.charAt(0).toUpperCase() + cliente.estado.slice(1)}
                    </td>
                    <td>
                        <button class="btn edit-btn" onclick="abrirModal(${cliente.idCliente})">Editar</button>
                        <button class="btn ${cliente.estado.toLowerCase() === 'activo' ? 'delete-btn' : 'activate-btn'}">
                            ${cliente.estado.toLowerCase() === 'activo' ? "Dar de Baja" : "Activar"}
                        </button>
                    </td>
                `;

                const boton = fila.querySelector("button.btn:last-of-type");
                boton.addEventListener("click", () => cambiarEstadoCliente(cliente.idCliente, cliente.estado.toLowerCase()));

                tbody.appendChild(fila);
            });
        })
        .catch(error => console.error("Error al cargar clientes:", error));
}
function abrirModal(id = null) {
    document.getElementById("clienteModal").style.display = "flex";
    const modalTitulo = document.getElementById("modalTitulo");

    if (id) {
        modalTitulo.textContent = "Modificar Cliente";
        fetch(`/clientes/${id}`)
            .then(response => response.json())
            .then(cliente => {
                document.getElementById("clienteId").value = cliente.idCliente;
                document.getElementById("nombre").value = cliente.nombre;
                document.getElementById("apellido").value = cliente.apellido;
                document.getElementById("cedula").value = cliente.cedula;
                document.getElementById("direccion").value = cliente.direccion;
                document.getElementById("telefono").value = cliente.telefono;
            })
            .catch(error => console.error("Error al obtener cliente:", error));
    } else {
        modalTitulo.textContent = "Agregar Cliente";
        limpiarFormulario();
    }
}

function cerrarModal() {
    document.getElementById("clienteModal").style.display = "none";
}

function limpiarFormulario() {
    document.getElementById("clienteId").value = "";
    document.getElementById("nombre").value = "";
    document.getElementById("apellido").value = "";
    document.getElementById("cedula").value = "";
    document.getElementById("direccion").value = "";
    document.getElementById("telefono").value = "";
}
function validarCliente(cliente, clientesRegistrados, idClienteActual = null) {
    const regexNombre = /^[A-Za-zÁáÉéÍíÓóÚúÑñ\s]+$/;
    const regexCedula = /^\d{1,10}$/;
    const regexDireccion = /^[A-Za-z0-9ÁáÉéÍíÓóÚúÑñ\s#\-,./]+$/;
    const regexTelefono = /^\d+$/;

    if (!cliente.nombre || !cliente.apellido || !cliente.cedula || !cliente.direccion || !cliente.telefono) {
        alert("Todos los campos son obligatorios.");
        return false;
    }

    if (!regexNombre.test(cliente.nombre) || !regexNombre.test(cliente.apellido)) {
        alert("El nombre y apellido solo deben contener letras y espacios.");
        return false;
    }

    if (!regexCedula.test(cliente.cedula)) {
        alert("La cédula debe contener solo números y tener máximo 10 dígitos.");
        return false;
    }

    if (!regexDireccion.test(cliente.direccion)) {
        alert("La dirección contiene caracteres inválidos.");
        return false;
    }

    if (!regexTelefono.test(cliente.telefono)) {
        alert("El teléfono solo debe contener números.");
        return false;
    }

    // Verificación de cédula duplicada, permitiendo que el cliente actual edite su propio registro
    const cedulaDuplicada = clientesRegistrados.some(c => c.cedula === cliente.cedula && c.idCliente !== idClienteActual);
    if (cedulaDuplicada) {
        alert("El cliente ya está registrado con esa cédula.");
        return false;
    }

    return true;
}

async function guardarCliente() {
    const id = document.getElementById("clienteId").value;
    const estadoActual = id ? document.querySelector(`tr[data-id='${id}'] .estado`)?.textContent.toLowerCase() : "activo";

    // Capturar valores del formulario
    const cliente = {
        idCliente: id ? parseInt(id) : null,
        nombre: document.getElementById("nombre").value.trim(),
        apellido: document.getElementById("apellido").value.trim(),
        cedula: document.getElementById("cedula").value.trim(),
        direccion: document.getElementById("direccion").value.trim(),
        telefono: document.getElementById("telefono").value.trim(),
        estado: estadoActual
    };

    try {
        // Obtener lista de clientes antes de validar
        const responseClientes = await fetch("/clientes");
        if (!responseClientes.ok) throw new Error("No se pudo obtener la lista de clientes.");
        const clientesRegistrados = await responseClientes.json();

        // Validar datos antes de enviar
        if (!validarCliente(cliente, clientesRegistrados, id ? parseInt(id) : null)) {
            return; // Si no pasa la validación, se detiene la ejecución
        }

        // Enviar los datos si la validación fue exitosa
        const response = await fetch(id ? `/clientes/${id}` : "/clientes", {
            method: id ? "PUT" : "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(cliente)
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`Error ${response.status}: ${errorMessage}`);
        }

        // Cerrar modal y recargar la lista
        cerrarModal();
        cargarClientes();
    } catch (error) {
        console.error("Error al guardar cliente:", error.message);
    }
}

function cambiarEstadoCliente(id, estadoActual) {
    const nuevoEstado = estadoActual === "activo" ? "inactivo" : "activo";
    const token = localStorage.getItem("token");

    if (!token) {
        console.error("No se encontró el token de autenticación.");
        return;
    }

    fetch(`/clientes/${id}/estado`, {
        method: "PUT",
        headers: getHeaders(),
        body: JSON.stringify({ estado: nuevoEstado })
    })
    .then(response => response.ok ? response.json() : Promise.reject(response))
    .then(clienteActualizado => actualizarFilaCliente(clienteActualizado))
    .catch(error => console.error("Error al cambiar estado:", error));
}

function actualizarFilaCliente(cliente) {
    const fila = document.querySelector(`tr[data-id='${cliente.idCliente}']`);
    if (!fila) return;

    const estadoTd = fila.querySelector(".estado");
    let boton = fila.querySelector("button.btn:last-of-type");

    estadoTd.textContent = cliente.estado.charAt(0).toUpperCase() + cliente.estado.slice(1);
    estadoTd.className = `estado ${cliente.estado.toLowerCase()}`;

    const nuevoBoton = document.createElement("button");
    nuevoBoton.className = `btn ${cliente.estado.toLowerCase() === "activo" ? "delete-btn" : "activate-btn"}`;
    nuevoBoton.textContent = cliente.estado.toLowerCase() === "activo" ? "Dar de Baja" : "Activar";
    nuevoBoton.addEventListener("click", () => cambiarEstadoCliente(cliente.idCliente, cliente.estado.toLowerCase()));

    boton.replaceWith(nuevoBoton);
}

function buscarClientes() {
    const texto = document.getElementById("buscar").value.toLowerCase();
    document.querySelectorAll("tbody tr").forEach(tr => {
        const contenidoFila = tr.innerText.toLowerCase();
        tr.style.display = contenidoFila.includes(texto) ? "" : "none";
    });
}

function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token
    };
}



