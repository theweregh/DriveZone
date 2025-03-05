/*document.addEventListener("DOMContentLoaded", function () {
    cargarCarrito();
});

// 🔹 Cargar productos en el carrito
async function cargarCarrito() {
    try {
        const response = await fetch('/api/carrito/obtener', { headers: getHeaders() });
        if (response.status === 204) {
            console.log("El carrito está vacío");
            mostrarCarrito([]);
            return;
        }
        if (!response.ok) throw new Error("Error al cargar el carrito");

        const carrito = await response.json();
        console.log("Carrito cargado:", carrito);
        mostrarCarrito(carrito);
    } catch (error) {
        console.error("Error en cargarCarrito:", error);
    }
}

// 🔹 Mostrar productos en la tabla del carrito
function mostrarCarrito(carrito) {
    const listaCarrito = document.getElementById("carrito-lista");
    const totalCarrito = document.getElementById("total-carrito");
    listaCarrito.innerHTML = "";
    let total = 0;

    carrito.forEach(item => {
        if (!item.accesorio) return; // Evita errores si el accesorio es nulo

        const accesorio = item.accesorio;
        const cantidad = item.cantidad || 1;
        const subtotal = accesorio.precioVenta * cantidad;
        total += subtotal;

        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${accesorio.nombre}</td>
            <td>$${accesorio.precioVenta.toLocaleString("es-CO")}</td>
            <td>
                <input type="number" value="${cantidad}" min="1"
                    onchange="actualizarCantidad(${accesorio.id}, this.value)">
            </td>
            <td>$${subtotal.toLocaleString("es-CO")}</td>
            <td>
                <button onclick="eliminarDelCarrito(${accesorio.id})">❌</button>
            </td>
        `;
        listaCarrito.appendChild(fila);
    });

    totalCarrito.innerText = total.toLocaleString("es-CO") + " COP";
}

// 🔹 Actualizar cantidad de un producto en el carrito
/*async function actualizarCantidad(id, nuevaCantidad) {
    if (!id || nuevaCantidad <= 0) {
        console.error("ID o cantidad inválida:", id, nuevaCantidad);
        return;
    }

    try {
        const response = await fetch('/api/carrito/actualizar', {
            method: 'PUT',
            headers: getHeaders(),
            body: JSON.stringify({ accesorioId: id, cantidad: nuevaCantidad })
        });

        if (!response.ok) throw new Error(`Error al actualizar cantidad de ID ${id}`);

        console.log(`Cantidad actualizada para ID ${id}: ${nuevaCantidad}`);
        cargarCarrito();
    } catch (error) {
        console.error("Error en actualizarCantidad:", error);
    }
}
async function actualizarCantidad(id) {
    const inputCantidad = document.getElementById(`cantidad-${id}`);
    const cantidad = parseInt(inputCantidad.value, 10);

    const token = localStorage.getItem("token");
    if (!token) {
        alert("❌ No has iniciado sesión.");
        return;
    }

    const data = {
        accesorio: { id: id }, // 🔹 Asegurar que el accesorio tiene ID
        cantidad: cantidad
    };

    try {
        const response = await fetch("/api/carrito/actualizar", {
            method: "PUT",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error(`Error al actualizar cantidad de ID ${id}`);
        }

        alert("✅ Cantidad actualizada");
    } catch (error) {
        console.error("❌ Error en actualizarCantidad:", error);
        alert("❌ No se pudo actualizar la cantidad.");
    }
}
// 🔹 Eliminar un producto del carrito
async function eliminarDelCarrito(id) {
    if (!id) {
        console.error("Intento de eliminar con ID inválido:", id);
        return;
    }

    try {
        const response = await fetch(`/api/carrito/eliminar/${id}`, {
            method: 'DELETE',
            headers: getHeaders()
        });

        if (!response.ok) throw new Error(`Error al eliminar el accesorio ${id}`);

        console.log(`Accesorio eliminado: ${id}`);
        cargarCarrito();
    } catch (error) {
        console.error("Error en eliminarDelCarrito:", error);
    }
}

// 🔹 Procesar compra
async function procesarCompra() {
    try {
        const response = await fetch('/api/carrito/procesar', {
            method: 'POST',
            headers: getHeaders()
        });

        if (!response.ok) throw new Error("Error al procesar la compra");

        alert("¡Compra realizada con éxito!");
        cargarCarrito();
    } catch (error) {
        alert("Error al procesar la compra");
        console.error("Error en procesarCompra:", error);
    }
}

document.getElementById("procesar-compra").addEventListener("click", procesarCompra);

// 🔹 Obtener encabezados para las solicitudes
function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token || ''
    };
}*/
// 🔹 Actualizar total del carrito
/*document.addEventListener("DOMContentLoaded", function () {
    cargarCarrito();
});

// 🔹 Cargar productos en el carrito
async function cargarCarrito() {
    try {
        const response = await fetch('/api/carrito/obtener', { headers: getHeaders() });

        if (response.status === 204) {
            console.log("El carrito está vacío");
            mostrarCarrito([]);
            return;
        }
        if (!response.ok) throw new Error("Error al cargar el carrito");

        const carrito = await response.json();
        console.log("Carrito cargado:", carrito);
        mostrarCarrito(carrito);
    } catch (error) {
        console.error("❌ Error en cargarCarrito:", error);
    }
}

// 🔹 Mostrar productos en la tabla del carrito
function mostrarCarrito(carrito) {
    const listaCarrito = document.getElementById("carrito-lista");
    const totalCarrito = document.getElementById("total-carrito");

    listaCarrito.innerHTML = "";
    let total = 0;

    carrito.forEach(item => {
        if (!item.accesorio) return;

        const accesorio = item.accesorio;
        const cantidad = item.cantidad || 1;
        const subtotal = accesorio.precioVenta * cantidad;
        total += subtotal;

        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${accesorio.nombre}</td>
            <td>$${accesorio.precioVenta.toLocaleString("es-CO")}</td>
            <td>
                <input type="number" id="cantidad-${accesorio.id}" value="${cantidad}" min="1"
                    onchange="actualizarCantidad(${accesorio.id})">
            </td>
            <td>$<span id="subtotal-${accesorio.id}">${subtotal.toLocaleString("es-CO")}</span></td>
            <td>
                <button onclick="eliminarDelCarrito(${accesorio.id})">❌</button>
            </td>
        `;
        listaCarrito.appendChild(fila);
    });

    totalCarrito.innerText = total.toLocaleString("es-CO") + " COP";
}

// 🔹 Actualizar cantidad de un producto en el carrito
async function actualizarCantidad(id) {
    const inputCantidad = document.querySelector(`#cantidad-${id}`);

    if (!inputCantidad) {
        console.error(`❌ No se encontró el input para el accesorio con ID ${id}`);
        return;
    }

    const cantidad = parseInt(inputCantidad.value, 10);
    if (isNaN(cantidad) || cantidad <= 0) {
        console.error(`❌ Cantidad inválida: ${cantidad}`);
        return;
    }

    const token = localStorage.getItem("token");
    if (!token) {
        alert("❌ No has iniciado sesión.");
        return;
    }

    const data = {
        accesorio: { id: id },
        cantidad: cantidad
    };

    try {
        const response = await fetch("/api/carrito/actualizar", {
            method: "PUT",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error(`Error al actualizar cantidad de ID ${id}`);
        }

        console.log(`✅ Cantidad actualizada para ID ${id}: ${cantidad}`);
        const subtotalElemento = document.querySelector(`#subtotal-${id}`);
        const precioUnidad = parseFloat(subtotalElemento.textContent.replace(/[^\d]/g, "")) / (cantidad - 1);
        subtotalElemento.textContent = (precioUnidad * cantidad).toLocaleString("es-CO");

        actualizarTotal();
    } catch (error) {
        console.error("❌ Error en actualizarCantidad:", error);
        alert("❌ No se pudo actualizar la cantidad.");
    }
}

// 🔹 Eliminar un producto del carrito
async function eliminarDelCarrito(id) {
    if (!id) {
        console.error("❌ Intento de eliminar con ID inválido:", id);
        return;
    }

    try {
        const response = await fetch(`/api/carrito/eliminar/${id}`, {
            method: 'DELETE',
            headers: getHeaders()
        });

        if (!response.ok) throw new Error(`Error al eliminar el accesorio ${id}`);

        console.log(`✅ Accesorio eliminado: ${id}`);
        cargarCarrito();
    } catch (error) {
        console.error("❌ Error en eliminarDelCarrito:", error);
    }
}

// 🔹 Procesar compra
async function procesarCompra() {
    try {
        const response = await fetch('/api/carrito/procesar', {
            method: 'POST',
            headers: getHeaders()
        });

        if (!response.ok) throw new Error("Error al procesar la compra");

        alert("✅ ¡Compra realizada con éxito!");
        cargarCarrito();
    } catch (error) {
        alert("❌ Error al procesar la compra");
        console.error("❌ Error en procesarCompra:", error);
    }
}

document.getElementById("procesar-compra").addEventListener("click", procesarCompra);

// 🔹 Obtener encabezados para las solicitudes
function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem("token") || ''
    };
}

// 🔹 Actualizar total del carrito
function actualizarTotal() {
    const totalCarrito = document.getElementById("total-carrito");
    let total = 0;

    document.querySelectorAll("[id^='subtotal-']").forEach(elemento => {
        total += parseInt(elemento.textContent.replace(/[^\d]/g, ""), 10) || 0;
    });

    totalCarrito.innerText = total.toLocaleString("es-CO") + " COP";
}*/
document.addEventListener("DOMContentLoaded", function () {
    cargarCarrito();
});

// 🔹 Cargar productos en el carrito
async function cargarCarrito() {
    try {
        const response = await fetch('/api/carrito/obtener', { headers: getHeaders() });
        if (response.status === 204) {
            console.log("El carrito está vacío");
            mostrarCarrito([]);
            return;
        }
        if (!response.ok) throw new Error("Error al cargar el carrito");

        const carrito = await response.json();
        console.log("Carrito cargado:", carrito);
        mostrarCarrito(carrito);
    } catch (error) {
        console.error("Error en cargarCarrito:", error);
    }
}

// 🔹 Mostrar productos en la tabla del carrito
function mostrarCarrito(carrito) {
    const listaCarrito = document.getElementById("carrito-lista");
    const totalCarrito = document.getElementById("total-carrito");
    listaCarrito.innerHTML = "";
    let total = 0;

    carrito.forEach(item => {
        if (!item.accesorio) return;

        const accesorio = item.accesorio;
        const cantidad = item.cantidad || 1;
        const subtotal = accesorio.precioVenta * cantidad;
        total += subtotal;

        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${accesorio.nombre}</td>
            <td>$${accesorio.precioVenta.toLocaleString("es-CO")}</td>
            <td>
                <input id="cantidad-${accesorio.id}" type="number" value="${cantidad}" min="1"
                    onchange="actualizarCantidad(${accesorio.id}, this.value)">
            </td>
            <td>$${subtotal.toLocaleString("es-CO")}</td>
            <td>
                <button onclick="eliminarDelCarrito(${accesorio.id})">❌</button>
            </td>
        `;
        listaCarrito.appendChild(fila);
    });

    totalCarrito.innerText = total.toLocaleString("es-CO") + " COP";
}

// 🔹 Actualizar cantidad en la DB
/*async function actualizarCantidad(id, nuevaCantidad) {
    if (!id || nuevaCantidad <= 0) {
        console.error("ID o cantidad inválida:", id, nuevaCantidad);
        return;
    }

    const token = localStorage.getItem("token");
    if (!token) {
        alert("❌ No has iniciado sesión.");
        return;
    }

    const data = {
        accesorioId: id,  // 🔹 Asegurarse de enviar el formato correcto
        cantidad: parseInt(nuevaCantidad, 10)
    };

    try {
        const response = await fetch("/api/carrito/actualizar", {
            method: "PUT",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error(`Error al actualizar cantidad de ID ${id}`);
        }

        console.log(`✅ Cantidad actualizada en DB para ID ${id}: ${nuevaCantidad}`);
        cargarCarrito(); // Recargar la tabla para reflejar los cambios
    } catch (error) {
        console.error("❌ Error en actualizarCantidad:", error);
        alert("❌ No se pudo actualizar la cantidad en la base de datos.");
    }
}*/
/*
async function actualizarCantidad(id, nuevaCantidad) {
    if (!id || nuevaCantidad <= 0) {
        console.error("ID o cantidad inválida:", id, nuevaCantidad);
        return;
    }

    const token = localStorage.getItem("token");
    if (!token) {
        alert("❌ No has iniciado sesión.");
        return;
    }

    const data = {
        accesorioId: id,  // 🔹 Confirmar que el backend espera este nombre
        cantidad: parseInt(nuevaCantidad, 10)
    };

    console.log("🔹 Enviando datos:", data);  // 🛠 Ver qué se envía realmente

    try {
        const response = await fetch("/api/carrito/actualizar", {
            method: "PUT",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error al actualizar cantidad de ID ${id}: ${errorText}`);
        }

        console.log(`✅ Cantidad actualizada en DB para ID ${id}: ${nuevaCantidad}`);
        cargarCarrito(); // Recargar la tabla
    } catch (error) {
        console.error("❌ Error en actualizarCantidad:", error);
        alert("❌ No se pudo actualizar la cantidad en la base de datos.");
    }
}*/
async function actualizarCantidad(id, nuevaCantidad) {
    if (!id || nuevaCantidad <= 0) {
        console.error("ID o cantidad inválida:", id, nuevaCantidad);
        return;
    }

    const token = localStorage.getItem("token");
    if (!token) {
        alert("❌ No has iniciado sesión.");
        return;
    }

    const data = {
        accesorio: { id: id }, // 🔹 El backend espera este formato
        cantidad: parseInt(nuevaCantidad, 10)
    };

    console.log("🔹 Enviando datos:", JSON.stringify(data));

    try {
        const response = await fetch("/api/carrito/actualizar", {
            method: "PUT",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error al actualizar cantidad de ID ${id}: ${errorText}`);
        }

        console.log(`✅ Cantidad actualizada en DB para ID ${id}: ${nuevaCantidad}`);
        cargarCarrito(); // Recargar la tabla
    } catch (error) {
        console.error("❌ Error en actualizarCantidad:", error);
        alert("❌ No se pudo actualizar la cantidad en la base de datos.");
    }
}
// 🔹 Eliminar un producto del carrito
async function eliminarDelCarrito(id) {
    if (!id) {
        console.error("Intento de eliminar con ID inválido:", id);
        return;
    }

    try {
        const response = await fetch(`/api/carrito/eliminar/${id}`, {
            method: 'DELETE',
            headers: getHeaders()
        });

        if (!response.ok) throw new Error(`Error al eliminar el accesorio ${id}`);

        console.log(`✅ Accesorio eliminado: ${id}`);
        cargarCarrito();
    } catch (error) {
        console.error("❌ Error en eliminarDelCarrito:", error);
    }
}

// 🔹 Procesar compra
async function procesarCompra() {
    try {
        const response = await fetch('/api/carrito/procesar', {
            method: 'POST',
            headers: getHeaders()
        });

        if (!response.ok) throw new Error("Error al procesar la compra");

        alert("✅ ¡Compra realizada con éxito!");
        cargarCarrito();
    } catch (error) {
        alert("❌ Error al procesar la compra");
        console.error("Error en procesarCompra:", error);
    }
}

document.getElementById("procesar-compra").addEventListener("click", procesarCompra);

// 🔹 Obtener encabezados para las solicitudes
function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token || ''
    };
}



