$(document).ready(function() {
$("#header").load("headerCliente.html", function() {
        console.log("✅ Header cargado correctamente.");
    });
    cargarAccesorios();
    actualizarContadorCarrito();
});

let accesoriosCargados = [];
let carrito = [];
let paginaActual = 1;
const accesoriosPorPagina = 6;

// 🔹 Cargar accesorios desde el backend
async function cargarAccesorios() {
    try {
        const request = await fetch('/api/accesorio', { method: 'GET', headers: getHeaders() });
        accesoriosCargados = await request.json();
        await cargarCarrito();  // 🔹 Cargar el carrito al inicio
        mostrarAccesorios();
    } catch (error) {
        console.error("❌ Error al cargar accesorios:", error);
    }
}

// 🔹 Cargar carrito desde el backend
async function cargarCarrito() {
    try {
        const response = await fetch('/api/carrito', { headers: getHeaders() });
        if (response.ok) {
            carrito = await response.json();
            actualizarContadorCarrito();
        }
    } catch (error) {
        console.error("❌ Error al cargar el carrito:", error);
    }
}

// 🔹 Mostrar accesorios en la página con paginación
function mostrarAccesorios() {
    const contenedor = document.getElementById("lista-accesorios");
    contenedor.innerHTML = "";

    const inicio = (paginaActual - 1) * accesoriosPorPagina;
    const fin = inicio + accesoriosPorPagina;
    const accesoriosPagina = accesoriosCargados.slice(inicio, fin);

    accesoriosPagina.forEach(accesorio => {
        const cantidadEnCarrito = obtenerCantidadEnCarrito(accesorio.id);
        const stockDisponible = accesorio.stock - cantidadEnCarrito; // 🔹 Stock real disponible

        const item = document.createElement("div");
        item.classList.add("accesorio");
        item.innerHTML = `
            <img src="${accesorio.imagen || 'https://via.placeholder.com/150'}" alt="${accesorio.nombre}">
            <h3>${accesorio.nombre}</h3>
            <p>${accesorio.descripcion}</p>
            <p><strong>Precio: </strong> $${accesorio.precioVenta}</p>
            <p><strong>Stock: </strong> ${stockDisponible} unidades</p>
            <input type="number" id="cantidad-${accesorio.id}" min="1" max="${stockDisponible}" value="1" oninput="validarCantidad(${accesorio.id})">
            <button onclick="agregarAlCarrito(${accesorio.id})">Agregar</button>
        `;

        contenedor.appendChild(item);
    });

    document.getElementById("pagina-actual").innerText = paginaActual;
}

// 🔹 Validar cantidad en el input
function validarCantidad(id) {
    const inputCantidad = document.getElementById(`cantidad-${id}`);
    const accesorio = accesoriosCargados.find(a => a.id === id);
    const cantidadEnCarrito = obtenerCantidadEnCarrito(id);
    const stockDisponible = accesorio.stock - cantidadEnCarrito;

    let cantidad = parseInt(inputCantidad.value, 10);

    if (cantidad < 1 || isNaN(cantidad)) {
        inputCantidad.value = 1;
    } else if (cantidad > stockDisponible) {
        inputCantidad.value = stockDisponible;
        alert(`⚠️ Solo puedes agregar ${stockDisponible} unidades más.`);
    }
}

// 🔹 Obtener cantidad del accesorio en el carrito
function obtenerCantidadEnCarrito(id) {
    const itemEnCarrito = carrito.find(item => item.accesorio.id === id);
    return itemEnCarrito ? itemEnCarrito.cantidad : 0;
}

// 🔹 Agregar accesorio al carrito con validación y token de usuario
async function agregarAlCarrito(id) {
    const inputCantidad = document.getElementById(`cantidad-${id}`);
    let cantidad = parseInt(inputCantidad.value, 10);

    const accesorio = accesoriosCargados.find(a => a.id === id);
    if (!accesorio) {
        alert("❌ Error: accesorio no encontrado");
        return;
    }

    const cantidadEnCarrito = obtenerCantidadEnCarrito(id);
    const stockDisponible = accesorio.stock - cantidadEnCarrito;

    if (cantidadEnCarrito >= accesorio.stock) {
        alert(`❌ Ya tienes el máximo de ${accesorio.stock} unidades en el carrito.`);
        return;
    }

    if (cantidad > stockDisponible) {
        cantidad = stockDisponible;
        inputCantidad.value = cantidad;
        alert(`⚠️ Solo puedes agregar ${stockDisponible} unidades más.`);
    }

    if (cantidad <= 0) {
        alert("❌ La cantidad debe ser mayor a 0.");
        return;
    }

    const token = localStorage.token;
    if (!token) {
        alert("❌ No has iniciado sesión.");
        return;
    }

    // Enviar accesorio y cantidad como `CarritoCompra`
    const data = {
        usuario: { id: obtenerIdUsuarioDesdeToken(token) }, // 🔹 Agregar ID de usuario desde el token
        accesorio: { id: accesorio.id },
        cantidad: cantidad
    };

    try {
        const response = await fetch('/api/carrito/agregar', {
            method: 'POST',
            headers: getHeaders(),
            body: JSON.stringify(data)
        });

        const result = await response.text();
        console.log("🔹 Respuesta del servidor:", result);

        if (response.status === 401) {
            alert("❌ Token inválido. Por favor, inicia sesión nuevamente.");
            return;
        }

        if (!response.ok) throw new Error(`Error al agregar al carrito: ${result}`);

        await cargarCarrito();
        alert(`✅ Añadido al carrito: ${accesorio.nombre} x${cantidad}`);
        actualizarContadorCarrito();
    } catch (error) {
        console.error("❌ Error en agregarAlCarrito:", error);
        alert("❌ No se pudo agregar al carrito");
    }
}

// 🔹 Obtener el ID del usuario desde el token JWT
function obtenerIdUsuarioDesdeToken(token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.id; // Asegúrate de que el backend almacene el ID del usuario en el payload
    } catch (error) {
        console.error("❌ Error al obtener ID del usuario desde el token:", error);
        return null;
    }
}

// 🔹 Actualizar contador del carrito
/*async function actualizarContadorCarrito() {
    document.getElementById("contador-carrito").innerText = carrito.length;
}*/
// 🔹 Actualizar contador del carrito obteniendo datos desde la base de datos
async function actualizarContadorCarrito() {
    try {
        const token = localStorage.getItem("token"); // Asegúrate de que el token está almacenado
        if (!token) {
            console.warn("⚠️ No hay token disponible.");
            return;
        }

        const response = await fetch("/api/carrito/carrito", {
            method: "GET",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            console.error("❌ Error al obtener el carrito:", response.statusText);
            return;
        }

        const carrito = await response.json();
        const totalProductos = carrito.reduce((total, item) => total + item.cantidad, 0); // Sumar cantidades

        document.getElementById("contador-carrito").innerText = totalProductos;
    } catch (error) {
        console.error("❌ Error al actualizar el contador del carrito:", error);
    }
}
// 🔹 Manejo de paginación
document.getElementById("prev-page").addEventListener("click", function() {
    if (paginaActual > 1) {
        paginaActual--;
        mostrarAccesorios();
    }
});

document.getElementById("next-page").addEventListener("click", function() {
    if (paginaActual * accesoriosPorPagina < accesoriosCargados.length) {
        paginaActual++;
        mostrarAccesorios();
    }
});

// 🔹 Ir al carrito de compras
function irAlCarrito() {
    window.location.href = "carrito.html";
}

// 🔹 Obtener headers con token de autorización
function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token || ''
    };
}



