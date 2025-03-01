/*
$(document).ready(function() {
    cargarAccesorios();
    actualizarContadorCarrito();
});

let accesoriosCargados = [];
let paginaActual = 1;
const accesoriosPorPagina = 6; // Ajusta según el diseño

async function cargarAccesorios() {
    try {
        const request = await fetch('/api/accesorio', { method: 'GET', headers: getHeaders() });
        accesoriosCargados = await request.json();
        mostrarAccesorios();
    } catch (error) {
        console.error("❌ Error al cargar accesorios:", error);
    }
}

function mostrarAccesorios() {
    const contenedor = document.getElementById("lista-accesorios");
    contenedor.innerHTML = "";

    const inicio = (paginaActual - 1) * accesoriosPorPagina;
    const fin = inicio + accesoriosPorPagina;
    const accesoriosPagina = accesoriosCargados.slice(inicio, fin);

    accesoriosPagina.forEach(accesorio => {
        const item = document.createElement("div");
        item.classList.add("accesorio");
        item.innerHTML = `
            <img src="${accesorio.imagen || 'https://via.placeholder.com/150'}" alt="${accesorio.nombre}">
            <h3>${accesorio.nombre}</h3>
            <p>${accesorio.descripcion}</p>
            <p><strong>Precio: </strong> $${accesorio.precioVenta}</p>
            <p><strong>Stock: </strong> ${accesorio.stock} unidades</p>
            <input type="number" id="cantidad-${accesorio.id}" min="1" max="${accesorio.stock}" value="1" oninput="validarCantidad(${accesorio.id})">
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
    let cantidad = parseInt(inputCantidad.value, 10);

    if (cantidad < 1 || isNaN(cantidad)) {
        inputCantidad.value = 1;
    } else if (cantidad > accesorio.stock) {
        inputCantidad.value = accesorio.stock;
        alert(`⚠️ Solo hay ${accesorio.stock} unidades disponibles.`);
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

function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token || ''
    };
}

// 🔹 Agregar accesorio al carrito con validación de stock
async function agregarAlCarrito(id) {
    const inputCantidad = document.getElementById(`cantidad-${id}`);
    let cantidad = parseInt(inputCantidad.value, 10);

    // 🔹 Buscar el accesorio en la lista cargada
    const accesorio = accesoriosCargados.find(a => a.id === id);
    if (!accesorio) {
        alert("❌ Error: accesorio no encontrado");
        return;
    }

    // 🔹 Validar stock antes de agregar al carrito
    if (cantidad > accesorio.stock) {
        cantidad = accesorio.stock;
        inputCantidad.value = cantidad;
        alert(`⚠️ No puedes agregar más de ${accesorio.stock} unidades.`);
    }

    if (cantidad <= 0) {
        alert("❌ La cantidad debe ser mayor a 0.");
        return;
    }

    const data = {
        accesorio: accesorio,  // ✅ Enviar objeto completo
        cantidad: cantidad
    };

    try {
        const response = await fetch('/api/carrito/agregar', {
            method: 'POST',
            headers: getHeaders(),
            body: JSON.stringify(data)
        });

        if (!response.ok) throw new Error("Error al agregar al carrito");

        actualizarContadorCarrito();
        alert(`✅ Añadido al carrito: ${accesorio.nombre} x${cantidad}`);
    } catch (error) {
        console.error("❌ Error en agregarAlCarrito:", error);
        alert("❌ No se pudo agregar al carrito");
    }
}

// 🔹 Actualizar contador del carrito
async function actualizarContadorCarrito() {
    try {
        const response = await fetch('/api/carrito', { headers: getHeaders() });
        if (response.ok) {
            const accesorios = await response.json();
            document.getElementById("contador-carrito").innerText = accesorios.length;
        }
    } catch (error) {
        console.error("❌ Error al actualizar el contador del carrito:", error);
    }
}

// 🔹 Ir al carrito de compras
function irAlCarrito() {
    window.location.href = "carrito.html"; // Ajusta la ruta si es diferente
}*/
$(document).ready(function() {
    cargarAccesorios();
    actualizarContadorCarrito();
});

let accesoriosCargados = [];
let carrito = [];
let paginaActual = 1;
const accesoriosPorPagina = 6;

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

// 🔹 Agregar accesorio al carrito con validación
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

    // 🔹 Validar stock con lo que ya hay en el carrito
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

    const data = {
        accesorio: accesorio,
        cantidad: cantidad
    };

    try {
        const response = await fetch('/api/carrito/agregar', {
            method: 'POST',
            headers: getHeaders(),
            body: JSON.stringify(data)
        });

        if (!response.ok) throw new Error("Error al agregar al carrito");

        await cargarCarrito();  // 🔹 Recargar el carrito después de agregar
        alert(`✅ Añadido al carrito: ${accesorio.nombre} x${cantidad}`);
    } catch (error) {
        console.error("❌ Error en agregarAlCarrito:", error);
        alert("❌ No se pudo agregar al carrito");
    }
}

// 🔹 Actualizar contador del carrito
async function actualizarContadorCarrito() {
    document.getElementById("contador-carrito").innerText = carrito.length;
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

function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token || ''
    };
}
