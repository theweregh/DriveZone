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
    if (id) {
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
        document.getElementById("clienteId").value = "";
        document.getElementById("nombre").value = "";
        document.getElementById("apellido").value = "";
        document.getElementById("cedula").value = "";
        document.getElementById("direccion").value = "";
        document.getElementById("telefono").value = "";
    }
}

function cerrarModal() {
    document.getElementById("clienteModal").style.display = "none";
}

function guardarCliente() {
    const id = document.getElementById("clienteId").value;
    const cliente = {
        nombre: document.getElementById("nombre").value,
        apellido: document.getElementById("apellido").value,
        cedula: document.getElementById("cedula").value,
        direccion: document.getElementById("direccion").value,
        telefono: document.getElementById("telefono").value,
        estado: "Activo"
    };

    fetch(id ? `/clientes/${id}` : "/clientes", {
        method: id ? "PUT" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(cliente)
    })
    .then(() => {
        cerrarModal();
        cargarClientes();
    })
    .catch(error => console.error("Error al guardar cliente:", error));
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
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => {
                throw new Error(`Error ${response.status}: ${err.message || response.statusText}`);
            });
        }
        return response.json();
    })
    .then(clienteActualizado => {
        console.log("Cliente actualizado:", clienteActualizado);
        actualizarFilaCliente(clienteActualizado);
    })
    .catch(error => console.error("Error al cambiar estado:", error.message || error));
}

function actualizarFilaCliente(cliente) {
    const fila = document.querySelector(`tr[data-id='${cliente.idCliente}']`);
    if (!fila) return;

    const estadoTd = fila.querySelector(".estado");
    let boton = fila.querySelector("button.btn:last-of-type");

    estadoTd.textContent = cliente.estado.charAt(0).toUpperCase() + cliente.estado.slice(1);
    estadoTd.className = `estado ${cliente.estado.toLowerCase()}`;

    // Crear un nuevo botón con los mismos atributos
    const nuevoBoton = document.createElement("button");
    nuevoBoton.className = `btn ${cliente.estado.toLowerCase() === "activo" ? "delete-btn" : "activate-btn"}`;
    nuevoBoton.textContent = cliente.estado.toLowerCase() === "activo" ? "Dar de Baja" : "Activar";

    // Agregar nuevamente el evento de cambio de estado
    nuevoBoton.addEventListener("click", () => cambiarEstadoCliente(cliente.idCliente, cliente.estado.toLowerCase()));

    // Reemplazar el botón en la fila
    boton.replaceWith(nuevoBoton);
}

function buscarClientes() {
    const texto = document.getElementById("buscar").value.toLowerCase();
    document.querySelectorAll("tbody tr").forEach(tr => {
        const idCliente = tr.children[0].textContent.trim();
        const contenidoFila = tr.innerText.toLowerCase();
        tr.style.display = contenidoFila.includes(texto) || idCliente.includes(texto) ? "" : "none";
    });
}
function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token
    };
}
