$(document).ready(function() {
    $("#header").load("headerCliente.html", function() {
        console.log("✅ Header cargado correctamente.");
    });
    cargarAccesorios();
    actualizarContadorCarrito();
});

let accesoriosCargados = [];
let carrito = JSON.parse(localStorage.getItem("carrito")) || [];
let paginaActual = 1;
const accesoriosPorPagina = 6;

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
        const cantidadEnCarrito = obtenerCantidadEnCarrito(accesorio.id);
        const stockDisponible = accesorio.stock - cantidadEnCarrito;
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

function validarCantidad(id) {
    const inputCantidad = document.getElementById(`cantidad-${id}`);
    const accesorio = accesoriosCargados.find(a => a.id === id);
    const cantidadEnCarrito = obtenerCantidadEnCarrito(id);
    const stockDisponible = accesorio.stock - cantidadEnCarrito;
    let cantidad = parseInt(inputCantidad.value, 10);
    if (cantidad < 1 || isNaN(cantidad)) inputCantidad.value = 1;
    else if (cantidad > stockDisponible) {
        inputCantidad.value = stockDisponible;
        alert(`⚠️ Solo puedes agregar ${stockDisponible} unidades más.`);
    }
}

function obtenerCantidadEnCarrito(id) {
    const itemEnCarrito = carrito.find(item => item.id === id);
    return itemEnCarrito ? itemEnCarrito.cantidad : 0;
}

function agregarAlCarrito(id) {
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
    let itemEnCarrito = carrito.find(item => item.id === id);
    if (itemEnCarrito) itemEnCarrito.cantidad += cantidad;
    else carrito.push({ id: accesorio.id, nombre: accesorio.nombre, precio: accesorio.precioVenta, cantidad });
    localStorage.setItem("carrito", JSON.stringify(carrito));
    alert(`✅ Añadido al carrito: ${accesorio.nombre} x${cantidad}`);
    actualizarContadorCarrito();
}

function actualizarContadorCarrito() {
    const totalProductos = carrito.reduce((total, item) => total + item.cantidad, 0);
    document.getElementById("contador-carrito").innerText = totalProductos;
}

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



