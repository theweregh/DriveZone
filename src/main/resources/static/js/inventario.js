// Cargar el header desde header.html
/*document.addEventListener("DOMContentLoaded", function () {
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

document.addEventListener("DOMContentLoaded", function () {
    cargarAccesorios();
});

const token = localStorage.getItem("token"); // Asegúrate de almacenar el token en localStorage
let accesoriosExistentes = []; // Lista de accesorios para verificar duplicados

async function cargarAccesorios() {
    try {
        const response = await fetch("api/accesorio", {
            method: "GET",
            headers: {
                "Authorization": token
            }
        });

        if (!response.ok) {
            throw new Error("Error al obtener los accesorios");
        }

        accesoriosExistentes = await response.json(); // Guardar la lista de accesorios existentes
        llenarTabla(accesoriosExistentes);
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al cargar los accesorios.");
    }
}

function llenarTabla(accesorios) {
    const tbody = document.querySelector("#tablaAccesorios tbody");
    tbody.innerHTML = "";

    accesorios.forEach(accesorio => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${accesorio.id}</td>
            <td>${accesorio.nombre}</td>
            <td>${accesorio.descripcion}</td>
            <td>${accesorio.stock}</td>
            <td>${accesorio.precioVenta}</td>
            <td><img src="${accesorio.imagen}" alt="Imagen" width="50"></td>
            <td>${accesorio.descuento}%</td>
            <td>
                <button class="btn btn-warning btn-sm" onclick="editarAccesorio(${accesorio.id})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="eliminarAccesorio(${accesorio.id})">Eliminar</button>
            </td>
        `;
        tbody.appendChild(row);
    });

    $("#tablaAccesorios").DataTable(); // Inicializa DataTable
}

async function editarAccesorio(id) {
    try {
        const response = await fetch(`api/accesorio/${id}`, {
            method: "GET",
            headers: { "Authorization": token }
        });

        if (!response.ok) {
            throw new Error("Error al obtener los detalles del accesorio");
        }

        const accesorio = await response.json();

        document.getElementById("editId").value = accesorio.id;
        document.getElementById("editNombre").value = accesorio.nombre;
        document.getElementById("editDescripcion").value = accesorio.descripcion;
        document.getElementById("editStock").value = accesorio.stock;
        document.getElementById("editPrecio").value = accesorio.precioVenta;
        document.getElementById("editImagen").value = accesorio.imagen;
        document.getElementById("editDescuento").value = accesorio.descuento;

        new bootstrap.Modal(document.getElementById("modalEditarAccesorio")).show();
    } catch (error) {
        console.error(error);
        alert("Error al obtener los detalles del accesorio.");
    }
}

document.getElementById("formEditarAccesorio").addEventListener("submit", async function (e) {
    e.preventDefault();

    const id = document.getElementById("editId").value;
    const stock = parseInt(document.getElementById("editStock").value);
    const descuento = parseFloat(document.getElementById("editDescuento").value);

    if (stock < 0 || !Number.isInteger(stock)) {
        alert("El stock debe ser un número entero positivo.");
        return;
    }

    if (descuento < 0 || descuento > 100) {
        alert("El descuento debe estar entre 0 y 100.");
        return;
    }

    const accesorioActualizado = {
        nombre: document.getElementById("editNombre").value,
        descripcion: document.getElementById("editDescripcion").value,
        stock,
        precioVenta: parseFloat(document.getElementById("editPrecio").value),
        imagen: document.getElementById("editImagen").value,
        descuento
    };

    try {
        const response = await fetch(`/accesorios/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": token
            },
            body: JSON.stringify(accesorioActualizado)
        });

        if (!response.ok) {
            throw new Error("Error al actualizar el accesorio");
        }

        alert("Accesorio actualizado con éxito");
        cargarAccesorios();
        bootstrap.Modal.getInstance(document.getElementById("modalEditarAccesorio")).hide();
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al actualizar el accesorio.");
    }
});

async function eliminarAccesorio(id) {
    if (!confirm("¿Seguro que quieres eliminar este accesorio?")) {
        return;
    }

    try {
        const response = await fetch(`/accesorios/${id}`, {
            method: "DELETE",
            headers: { "Authorization": token }
        });

        if (!response.ok) {
            throw new Error("Error al eliminar el accesorio");
        }

        alert("Accesorio eliminado correctamente");
        cargarAccesorios();
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al eliminar el accesorio.");
    }
}

async function agregarAccesorio() {
    const nombre = prompt("Ingrese el nombre del accesorio:");
    const descripcion = prompt("Ingrese la descripción:");
    const stock = parseInt(prompt("Ingrese el stock:"));
    const precioVenta = parseFloat(prompt("Ingrese el precio:"));
    const imagen = prompt("Ingrese la URL de la imagen:");
    const descuento = parseFloat(prompt("Ingrese el descuento:"));

    if (!nombre || !descripcion || isNaN(stock) || isNaN(precioVenta) || isNaN(descuento)) {
        alert("Todos los campos son obligatorios.");
        return;
    }

    if (stock < 0 || !Number.isInteger(stock)) {
        alert("El stock debe ser un número entero positivo.");
        return;
    }

    if (descuento < 0 || descuento > 100) {
        alert("El descuento debe estar entre 0 y 100.");
        return;
    }

    // Verificar si el accesorio ya existe
    const accesorioExiste = accesoriosExistentes.some(acc => acc.nombre.toLowerCase() === nombre.toLowerCase());
    if (accesorioExiste) {
        alert("El ítem ya está registrado.");
        return;
    }

    const nuevoAccesorio = { nombre, descripcion, stock, precioVenta, imagen, descuento };

    try {
        const response = await fetch("/accesorios", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": token
            },
            body: JSON.stringify(nuevoAccesorio)
        });

        if (!response.ok) {
            throw new Error("Error al agregar el accesorio");
        }

        alert("Accesorio agregado correctamente");
        cargarAccesorios();
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al agregar el accesorio.");
    }
}*//*
document.addEventListener("DOMContentLoaded", function () {
    fetch("header.html")
        .then(response => response.text())
        .then(data => {
            document.getElementById("header").innerHTML = data;
        })
        .catch(error => console.error("Error cargando el header:", error));

    cargarAccesorios();
});

const token = localStorage.getItem("token");
let accesoriosExistentes = [];

async function cargarAccesorios() {
    try {
        const response = await fetch("api/accesorio", {
            method: "GET",
            headers: { "Authorization": token }
        });

        if (!response.ok) throw new Error("Error al obtener los accesorios");

        accesoriosExistentes = await response.json();
        llenarTabla(accesoriosExistentes);
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al cargar los accesorios.");
    }
}

function llenarTabla(accesorios) {
    const tbody = document.querySelector("#tablaAccesorios tbody");
    tbody.innerHTML = "";

    accesorios.forEach(accesorio => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${accesorio.id}</td>
            <td>${accesorio.nombre}</td>
            <td>${accesorio.descripcion}</td>
            <td>${accesorio.stock}</td>
            <td>${accesorio.precioVenta}</td>
            <td><img src="${accesorio.imagen}" alt="Imagen" width="50"></td>
            <td>${accesorio.descuento}%</td>
            <td>
                <button class="btn btn-warning btn-sm" onclick="editarAccesorio(${accesorio.id})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="eliminarAccesorio(${accesorio.id})">Eliminar</button>
            </td>
        `;
        tbody.appendChild(row);
    });

    $("#tablaAccesorios").DataTable();
}

function abrirModalAgregarAccesorio() {
    new bootstrap.Modal(document.getElementById("modalAgregarAccesorio")).show();
}

document.getElementById("formAgregarAccesorio").addEventListener("submit", async function (e) {
    e.preventDefault();

    const nuevoAccesorio = {
        nombre: document.getElementById("addNombre").value,
        descripcion: document.getElementById("addDescripcion").value,
        stock: parseInt(document.getElementById("addStock").value),
        precioVenta: parseFloat(document.getElementById("addPrecio").value),
        imagen: document.getElementById("addImagen").value,
        descuento: parseFloat(document.getElementById("addDescuento").value)
    };

    try {
        const response = await fetch("/accesorios", {
            method: "POST",
            headers: { "Content-Type": "application/json", "Authorization": token },
            body: JSON.stringify(nuevoAccesorio)
        });

        if (!response.ok) throw new Error("Error al agregar el accesorio");

        alert("Accesorio agregado correctamente");
        cargarAccesorios();
        bootstrap.Modal.getInstance(document.getElementById("modalAgregarAccesorio")).hide();
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al agregar el accesorio.");
    }
});
async function editarAccesorio(id) {
    try {
        const response = await fetch(`api/accesorio/${id}`, {
            method: "GET",
            headers: { "Authorization": token }
        });

        if (!response.ok) {
            throw new Error("Error al obtener los detalles del accesorio");
        }

        const accesorio = await response.json();

        document.getElementById("editId").value = accesorio.id;
        document.getElementById("editNombre").value = accesorio.nombre;
        document.getElementById("editDescripcion").value = accesorio.descripcion;
        document.getElementById("editStock").value = accesorio.stock;
        document.getElementById("editPrecio").value = accesorio.precioVenta;
        document.getElementById("editImagen").value = accesorio.imagen;
        document.getElementById("editDescuento").value = accesorio.descuento;

        new bootstrap.Modal(document.getElementById("modalEditarAccesorio")).show();
    } catch (error) {
        console.error(error);
        alert("Error al obtener los detalles del accesorio.");
    }
}

document.getElementById("formEditarAccesorio").addEventListener("submit", async function (e) {
    e.preventDefault();

    const id = document.getElementById("editId").value;
    const stock = parseInt(document.getElementById("editStock").value);
    const descuento = parseFloat(document.getElementById("editDescuento").value);

    if (stock < 0 || !Number.isInteger(stock)) {
        alert("El stock debe ser un número entero positivo.");
        return;
    }

    if (descuento < 0 || descuento > 100) {
        alert("El descuento debe estar entre 0 y 100.");
        return;
    }

    const accesorioActualizado = {
        nombre: document.getElementById("editNombre").value,
        descripcion: document.getElementById("editDescripcion").value,
        stock,
        precioVenta: parseFloat(document.getElementById("editPrecio").value),
        imagen: document.getElementById("editImagen").value,
        descuento
    };

    try {
        const response = await fetch(`/accesorios/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": token
            },
            body: JSON.stringify(accesorioActualizado)
        });

        if (!response.ok) {
            throw new Error("Error al actualizar el accesorio");
        }

        alert("Accesorio actualizado con éxito");
        cargarAccesorios();
        bootstrap.Modal.getInstance(document.getElementById("modalEditarAccesorio")).hide();
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al actualizar el accesorio.");
    }
});

async function eliminarAccesorio(id) {
    if (!confirm("¿Seguro que quieres eliminar este accesorio?")) {
        return;
    }

    try {
        const response = await fetch(`/accesorios/${id}`, {
            method: "DELETE",
            headers: { "Authorization": token }
        });

        if (!response.ok) {
            throw new Error("Error al eliminar el accesorio");
        }

        alert("Accesorio eliminado correctamente");
        cargarAccesorios();
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al eliminar el accesorio.");
    }
}*/
// Cargar el header al cargar la página
document.addEventListener("DOMContentLoaded", function () {
    fetch("header.html")
        .then(response => response.text())
        .then(data => {
            document.getElementById("header").innerHTML = data;
        })
        .catch(error => console.error("Error cargando el header:", error));

    cargarAccesorios();
});

const token = localStorage.getItem("token");
let accesoriosExistentes = [];

/* ✅ Cargar accesorios desde la API */
async function cargarAccesorios() {
    try {
        const response = await fetch("api/accesorio", {
            method: "GET",
            headers: { "Authorization": token }
        });

        if (!response.ok) throw new Error("Error al obtener los accesorios");

        accesoriosExistentes = await response.json();
        llenarTabla(accesoriosExistentes);
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al cargar los accesorios.");
    }
}

/* ✅ Llenar la tabla con los accesorios */
function llenarTabla(accesorios) {
    const tbody = document.querySelector("#tablaAccesorios tbody");
    tbody.innerHTML = "";

    accesorios.forEach(accesorio => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${accesorio.id}</td>
            <td>${accesorio.nombre}</td>
            <td>${accesorio.descripcion}</td>
            <td>${accesorio.stock}</td>
            <td>${accesorio.precioVenta}</td>
            <td><img src="${accesorio.imagen}" alt="Imagen" width="50"></td>
            <td>${accesorio.descuento}%</td>
            <td>
                <button class="btn btn-warning btn-sm" onclick="editarAccesorio(${accesorio.id})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="eliminarAccesorio(${accesorio.id})">Eliminar</button>
            </td>
        `;
        tbody.appendChild(row);
    });

    $("#tablaAccesorios").DataTable();
}

/* ✅ Abrir modal para agregar un accesorio */
function abrirModalAgregarAccesorio() {
    new bootstrap.Modal(document.getElementById("modalAgregarAccesorio")).show();
}

/* ✅ Agregar accesorio */
document.getElementById("formAgregarAccesorio").addEventListener("submit", async function (e) {
    e.preventDefault();

    const nuevoAccesorio = {
        nombre: document.getElementById("addNombre").value,
        descripcion: document.getElementById("addDescripcion").value,
        stock: parseInt(document.getElementById("addStock").value),
        precioVenta: parseFloat(document.getElementById("addPrecio").value),
        imagen: document.getElementById("addImagen").value,
        descuento: parseFloat(document.getElementById("addDescuento").value)
    };

    try {
        const response = await fetch("/accesorios", {
            method: "POST",
            headers: { "Content-Type": "application/json", "Authorization": token },
            body: JSON.stringify(nuevoAccesorio)
        });

        if (!response.ok) throw new Error("Error al agregar el accesorio");

        alert("Accesorio agregado correctamente");
        cargarAccesorios();
        bootstrap.Modal.getInstance(document.getElementById("modalAgregarAccesorio")).hide();
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al agregar el accesorio.");
    }
});

/* ✅ Editar accesorio *//*
async function editarAccesorio(id) {
    try {
        const response = await fetch(`api/accesorio/${id}`, {
            method: "GET",
            headers: { "Authorization": token }
        });

        if (!response.ok) throw new Error("Error al obtener los detalles del accesorio");

        const accesorio = await response.json();

        document.getElementById("editId").value = accesorio.id;
        document.getElementById("editNombre").value = accesorio.nombre;
        document.getElementById("editDescripcion").value = accesorio.descripcion;
        document.getElementById("editStock").value = accesorio.stock;
        document.getElementById("editPrecio").value = accesorio.precioVenta;
        document.getElementById("editImagen").value = accesorio.imagen;
        document.getElementById("editDescuento").value = accesorio.descuento;

        new bootstrap.Modal(document.getElementById("modalEditarAccesorio")).show();
    } catch (error) {
        console.error(error);
        alert("Error al obtener los detalles del accesorio.");
    }
}*/
async function editarAccesorio(id) {
    try {
        const response = await fetch(`api/accesorio/${id}`, {
            method: "GET",
            headers: { "Authorization": token }
        });

        if (!response.ok) throw new Error("Error al obtener los detalles del accesorio");

        const accesorio = await response.json();

        document.getElementById("editId").value = accesorio.id;
        document.getElementById("editNombre").value = accesorio.nombre;
        document.getElementById("editDescripcion").value = accesorio.descripcion;
        document.getElementById("editStock").value = accesorio.stock;
        document.getElementById("editPrecio").value = accesorio.precioVenta;
        document.getElementById("editImagen").value = accesorio.imagen;
        document.getElementById("editDescuento").value = accesorio.descuento;

        let modalEditar = new bootstrap.Modal(document.getElementById("modalEditarAccesorio"));
        modalEditar.show();
    } catch (error) {
        console.error(error);
        alert("Error al obtener los detalles del accesorio.");
    }
}

/* ✅ Actualizar accesorio */
document.getElementById("formEditarAccesorio").addEventListener("submit", async function (e) {
    e.preventDefault();

    const id = document.getElementById("editId").value;
    const accesorioActualizado = {
        nombre: document.getElementById("editNombre").value,
        descripcion: document.getElementById("editDescripcion").value,
        stock: parseInt(document.getElementById("editStock").value),
        precioVenta: parseFloat(document.getElementById("editPrecio").value),
        imagen: document.getElementById("editImagen").value,
        descuento: parseFloat(document.getElementById("editDescuento").value)
    };

    try {
        const response = await fetch(`/accesorios/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json", "Authorization": token },
            body: JSON.stringify(accesorioActualizado)
        });

        if (!response.ok) throw new Error("Error al actualizar el accesorio");

        alert("Accesorio actualizado con éxito");
        cargarAccesorios();
        bootstrap.Modal.getInstance(document.getElementById("modalEditarAccesorio")).hide();
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al actualizar el accesorio.");
    }
});

/* ✅ Eliminar accesorio */
async function eliminarAccesorio(id) {
    if (!confirm("¿Seguro que quieres eliminar este accesorio?")) return;

    try {
        const response = await fetch(`/accesorios/${id}`, {
            method: "DELETE",
            headers: { "Authorization": token }
        });

        if (!response.ok) throw new Error("Error al eliminar el accesorio");

        alert("Accesorio eliminado correctamente");
        cargarAccesorios();
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al eliminar el accesorio.");
    }
}







