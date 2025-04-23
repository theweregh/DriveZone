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
                <button class="btn btn-warning btn-sm" onclick="abrirModalEditar(${accesorio.id})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="eliminarAccesorio(${accesorio.id}, '${accesorio.nombre}')">Eliminar</button>
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

    const nombre = document.getElementById("addNombre").value.trim();
    const descripcion = document.getElementById("addDescripcion").value.trim();
    const stock = parseInt(document.getElementById("addStock").value);
    const precioVenta = parseFloat(document.getElementById("addPrecio").value);
    const imagen = document.getElementById("addImagen").value.trim();
    const descuento = parseFloat(document.getElementById("addDescuento").value);

    // Validaciones
    if (!nombre || !descripcion || !imagen) {
        alert("Todos los campos deben estar llenos.");
        return;
    }
    if (isNaN(stock) || stock < 0) {
        alert("El stock no puede ser negativo.");
        return;
    }
    if (isNaN(descuento) || descuento < 0 || descuento > 100) {
        alert("El descuento debe estar entre 0 y 100.");
        return;
    }

    const nuevoAccesorio = { nombre, descripcion, stock, precioVenta, imagen, descuento };

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
/* ✅ Abrir modal de edición con datos del accesorio */
function abrirModalEditar(id) {
    const accesorio = accesoriosExistentes.find(a => a.id === id);
    if (!accesorio) {
        alert("No se encontró el accesorio.");
        return;
    }

    document.getElementById("editId").value = accesorio.id;
    document.getElementById("editNombre").value = accesorio.nombre;
    document.getElementById("editDescripcion").value = accesorio.descripcion;
    document.getElementById("editStock").value = accesorio.stock;
    document.getElementById("editPrecio").value = accesorio.precioVenta;
    document.getElementById("editImagen").value = accesorio.imagen;
    document.getElementById("editDescuento").value = accesorio.descuento;

    new bootstrap.Modal(document.getElementById("modalEditarAccesorio")).show();
}
/* ✅ Editar accesorio *//*
document.getElementById("formEditarAccesorio").addEventListener("submit", async function (e) {
    e.preventDefault();

    const id = document.getElementById("editId").value;
    const nombre = document.getElementById("editNombre").value.trim();
    const descripcion = document.getElementById("editDescripcion").value.trim();
    const stock = parseInt(document.getElementById("editStock").value);
    const precioVenta = parseFloat(document.getElementById("editPrecio").value);
    const imagen = document.getElementById("editImagen").value.trim();
    const descuento = parseFloat(document.getElementById("editDescuento").value);

    // Validaciones
    if (!nombre || !descripcion || !imagen) {
        alert("Todos los campos deben estar llenos.");
        return;
    }
    if (isNaN(stock) || stock < 0) {
        alert("El stock no puede ser negativo.");
        return;
    }
    if (isNaN(descuento) || descuento < 0 || descuento > 100) {
        alert("El descuento debe estar entre 0 y 100.");
        return;
    }

    const accesorioActualizado = { nombre, descripcion, stock, precioVenta, imagen, descuento };
    if (accesorioOriginal.getDescuento() != accesorioActualizado.getDescuento()) {
    double precioOriginal = accesorioOriginal.getPrecioVenta();
    double descuentoNuevo = accesorioActualizado.getDescuento();
    double nuevoPrecio = precioOriginal * (1 - descuentoNuevo / 100);

    String asunto = "¡Descuento actualizado en " + accesorioActualizado.getNombre() + "!";
    String cuerpo = "Hola, tenemos una novedad en nuestro catálogo:\n\n" +
                    "🔸 Producto: " + accesorioActualizado.getNombre() + "\n" +
                    "💰 Precio original: $" + precioOriginal + "\n" +
                    "📉 Descuento aplicado: " + descuentoNuevo + "%\n" +
                    "🛒 Nuevo precio con descuento: $" + nuevoPrecio + "\n\n" +
                    "¡No te lo pierdas!";

    List<Usuario> clientes = usuarioDao.getUsers(); // Asegúrate de filtrar si es necesario

    for (Usuario cliente : clientes) {
        emailService.enviarCorreo(cliente.getCorreo(), asunto, cuerpo);
    }
}

    try {
        const response = await fetch(`/accesorios/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json", "Authorization": token },
            body: JSON.stringify(accesorioActualizado)
        });

        if (!response.ok) throw new Error("Error al actualizar el accesorio");

        alert("Accesorio actualizado correctamente");
        cargarAccesorios();
        bootstrap.Modal.getInstance(document.getElementById("modalEditarAccesorio")).hide();
    } catch (error) {
        console.error(error);
        alert("Hubo un problema al actualizar el accesorio.");
    }
});*/
document.getElementById("formEditarAccesorio").addEventListener("submit", async function (e) {
    e.preventDefault();

    const id = document.getElementById("editId").value;
    const nombre = document.getElementById("editNombre").value.trim();
    const descripcion = document.getElementById("editDescripcion").value.trim();
    const stock = parseInt(document.getElementById("editStock").value);
    const precioVenta = parseFloat(document.getElementById("editPrecio").value);
    const imagen = document.getElementById("editImagen").value.trim();
    const descuento = parseFloat(document.getElementById("editDescuento").value);

    if (!nombre || !descripcion || !imagen) {
        alert("Todos los campos deben estar llenos.");
        return;
    }
    if (isNaN(stock) || stock < 0) {
        alert("El stock no puede ser negativo.");
        return;
    }
    if (isNaN(descuento) || descuento < 0 || descuento > 100) {
        alert("El descuento debe estar entre 0 y 100.");
        return;
    }

    try {
        // 1. Obtener datos del accesorio original
        const resOriginal = await fetch(`/api/accesorio/${id}`, {
            headers: { "Authorization": token }
        });

        if (!resOriginal.ok) throw new Error("Error obteniendo el accesorio original");

        const accesorioOriginal = await resOriginal.json();

        const accesorioActualizado = {
            nombre,
            descripcion,
            stock,
            precioVenta,
            imagen,
            descuento
        };

        // 2. Comparamos el descuento original vs nuevo
        const descuentoOriginal = accesorioOriginal.descuento;
        if (descuentoOriginal !== descuento) {
            // Llamamos a tu endpoint personalizado para que se encargue del correo
            await fetch(`/api/notificar-descuento/${id}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": token
                },
                body: JSON.stringify(accesorioActualizado)
            });
        }

        // 3. Actualizamos el accesorio
        const response = await fetch(`/accesorios/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json", "Authorization": token },
            body: JSON.stringify(accesorioActualizado)
        });

        if (!response.ok) throw new Error("Error al actualizar el accesorio");

        alert("Accesorio actualizado correctamente");
        cargarAccesorios();
        bootstrap.Modal.getInstance(document.getElementById("modalEditarAccesorio")).hide();

    } catch (error) {
        console.error(error);
        alert("Hubo un problema al actualizar el accesorio.");
    }
});

/* ✅ Eliminar accesorio */
async function eliminarAccesorio(id, nombreAccesorio) {
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









