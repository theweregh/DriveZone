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



