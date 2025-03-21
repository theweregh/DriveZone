document.addEventListener("DOMContentLoaded", function () {
    $("#header").load("headerCliente.html", function() {
        console.log("✅ Header cargado correctamente.");
        actualizarContadorCarrito();
    });
    cargarCarrito();
});

// Función para obtener el carrito desde localStorage
function obtenerCarritoDesdeLocalStorage() {
    return JSON.parse(localStorage.getItem("carrito")) || [];
}

// Función para guardar el carrito en localStorage
function guardarCarritoEnLocalStorage(carrito) {
    localStorage.setItem("carrito", JSON.stringify(carrito));
}

// Función para cargar el carrito y mostrarlo
function cargarCarrito() {
    const carrito = obtenerCarritoDesdeLocalStorage();
    console.log("📦 Datos en localStorage:", localStorage.getItem("carrito"));
    console.log("➡️ Carrito cargado:", carrito);
    mostrarCarrito(carrito);
    actualizarContadorCarrito();
}
function mostrarCarrito(carrito) {
    const listaCarrito = document.getElementById("carrito-lista");
    const totalCarrito = document.getElementById("total-carrito");

    if (!listaCarrito) {
        console.error("❌ Elemento 'carrito-lista' no encontrado en el DOM.");
        return;
    }

    if (!Array.isArray(carrito) || carrito.length === 0) {
        console.warn("⚠️ El carrito está vacío o tiene un formato incorrecto.");
        listaCarrito.innerHTML = "<tr><td colspan='5'>No hay productos en el carrito.</td></tr>";
        totalCarrito.innerText = "0 COP";
        return;
    }

    listaCarrito.innerHTML = "";
    let total = 0;

    carrito.forEach(item => {
        if (!item.id || !item.nombre || !item.precio) {
            console.warn("⚠️ Item inválido en el carrito:", item);
            return;
        }

        const id = item.id;
        const nombre = item.nombre;
        const precio = item.precio;
        const cantidad = item.cantidad || 1;
        const subtotal = precio * cantidad;
        total += subtotal;

        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${nombre}</td>
            <td>$${precio.toLocaleString("es-CO")}</td>
            <td>
                <input id="cantidad-${id}" type="number" value="${cantidad}" min="1"
                    onchange="actualizarCantidad(${id}, this.value)">
            </td>
            <td>$${subtotal.toLocaleString("es-CO")}</td>
            <td>
                <button onclick="eliminarDelCarrito(${id})">❌</button>
            </td>
        `;
        listaCarrito.appendChild(fila);
    });

    totalCarrito.innerText = total.toLocaleString("es-CO") + " COP";
}
function actualizarCantidad(id, cantidad) {
    cantidad = parseInt(cantidad, 10);

    if (isNaN(cantidad) || cantidad < 1) {
        console.warn("⚠️ Cantidad inválida, se mantiene el valor anterior.");
        return;
    }

    let carrito = JSON.parse(localStorage.getItem("carrito")) || [];
    let producto = carrito.find(item => item.id === id);

    if (!producto) {
        console.error("❌ No se encontró el producto en el carrito.");
        return;
    }

    producto.cantidad = cantidad;
    localStorage.setItem("carrito", JSON.stringify(carrito));
    mostrarCarrito(carrito);
    actualizarContadorCarrito();
}
function eliminarDelCarrito(id) {
    let carrito = JSON.parse(localStorage.getItem("carrito")) || [];
    let nuevoCarrito = carrito.filter(item => item.id !== id);

    if (nuevoCarrito.length === carrito.length) {
        console.warn("⚠️ No se encontró el producto a eliminar.");
        return;
    }

    localStorage.setItem("carrito", JSON.stringify(nuevoCarrito));
    mostrarCarrito(nuevoCarrito);
    actualizarContadorCarrito();
}

// Función para actualizar el contador del carrito en el header
function actualizarContadorCarrito() {
    const carrito = obtenerCarritoDesdeLocalStorage();
    const totalProductos = carrito.reduce((total, item) => total + item.cantidad, 0);
    const contadorCarrito = document.getElementById("contador-carrito");
    if (contadorCarrito) {
        contadorCarrito.innerText = totalProductos;
    }
}

// Evento para procesar la compra
document.getElementById("procesar-compra").addEventListener("click", function() {
    alert("✅ ¡Compra procesada con éxito!");
    localStorage.removeItem("carrito");
    cargarCarrito();
});
// Obtiene el stock de un accesorio desde la API
async function obtenerStockDesdeAPI(id) {
    try {
        const token = localStorage.getItem("token");
        const response = await fetch(`api/accesorio/${id}`, {
            method: "GET",
            headers: { "Authorization": token }
        });

        if (!response.ok) throw new Error(`Error al obtener stock (${response.status})`);

        const accesorio = await response.json();
        console.log("🔍 Accesorio recibido:", accesorio);

        return accesorio?.stock ?? 0; // Si stock es undefined, devuelve 0
    } catch (error) {
        console.error("⚠️ Error obteniendo stock:", error);
        return 0;
    }
}

// Función para agregar un producto al carrito con validación de stock desde la API
async function agregarAlCarrito(id, nombre, precio) {
    const stockMaximo = await obtenerStockDesdeAPI(id);
    console.log(`🔍 Stock máximo obtenido para "${nombre}":`, stockMaximo); // Depuración

    let carrito = obtenerCarritoDesdeLocalStorage();
    let producto = carrito.find(item => item.id === id);

    if (producto) {
        console.log(`🛒 Producto ya en carrito: ${producto.nombre}, cantidad actual: ${producto.cantidad}`);
        if (producto.cantidad + 1 > stockMaximo) {
            alert(`⚠️ No puedes agregar más unidades de "${nombre}". Stock máximo: ${stockMaximo}.`);
            return;
        }
        producto.cantidad++;
    } else {
        if (stockMaximo < 1) {
            alert(`❌ "${nombre}" está agotado.`);
            return;
        }
        carrito.push({ id, nombre, precio, cantidad: 1 });
    }

    guardarCarritoEnLocalStorage(carrito);
    mostrarCarrito(carrito);
    actualizarContadorCarrito();
}

// Función para actualizar la cantidad con validación de stock desde la API
async function actualizarCantidad(id, cantidad) {
    cantidad = parseInt(cantidad, 10);
    const stockMaximo = await obtenerStockDesdeAPI(id);

    console.log(`🔍 Intentando actualizar cantidad para "${id}". Cantidad ingresada: ${cantidad}, Stock disponible: ${stockMaximo}`);

    if (isNaN(cantidad) || cantidad < 1) {
        console.warn("⚠️ Cantidad inválida, se mantiene el valor anterior.");
        return;
    }

    if (cantidad > stockMaximo) {
        alert(`⚠️ Solo puedes agregar hasta ${stockMaximo} unidades.`);
        document.getElementById(`cantidad-${id}`).value = stockMaximo;
        cantidad = stockMaximo;
    }

    let carrito = obtenerCarritoDesdeLocalStorage();
    let producto = carrito.find(item => item.id === id);

    if (!producto) {
        console.error("❌ No se encontró el producto en el carrito.");
        return;
    }

    producto.cantidad = cantidad;
    guardarCarritoEnLocalStorage(carrito);
    mostrarCarrito(carrito);
    actualizarContadorCarrito();
}/*
function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token
    };
}*/



