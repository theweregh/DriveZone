/*$(document).ready(function() {
    $("#header").load("headerCarritoEmpleado.html", function() {
        console.log("✅ Header cargado correctamente.");
        actualizarContadorCarrito();
    });
    cargarAccesorios();
});

let accesoriosCargados = [];
let carrito = JSON.parse(localStorage.getItem("carrito")) || [];
let paginaActual = 1;
const accesoriosPorPagina = 6;

async function cargarAccesorios() {
    try {
        const request = await fetch('/api/accesorio', { method: 'GET', headers: getHeaders() });
        accesoriosCargados = await request.json();
        if (!Array.isArray(accesoriosCargados)) throw new Error("Datos de accesorios no válidos");
        mostrarAccesorios();
    } catch (error) {
        console.error("❌ Error al cargar accesorios:", error);
        alert("No se pudieron cargar los accesorios. Inténtalo más tarde.");
    }
}

function mostrarAccesorios() {
    const contenedor = document.getElementById("lista-accesorios");
    if (!contenedor) {
        console.error("❌ No se encontró el contenedor de accesorios");
        return;
    }
    contenedor.innerHTML = "";
    const inicio = (paginaActual - 1) * accesoriosPorPagina;
    const fin = inicio + accesoriosPorPagina;
    const accesoriosPagina = accesoriosCargados.slice(inicio, fin);

    accesoriosPagina.forEach(accesorio => {
        if (!accesorio || typeof accesorio !== 'object') return;
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
    if (!inputCantidad || !accesorio) return;

    const cantidadEnCarrito = obtenerCantidadEnCarrito(id);
    const stockDisponible = accesorio.stock - cantidadEnCarrito;
    let cantidad = parseInt(inputCantidad.value, 10);

    if (isNaN(cantidad) || cantidad < 1) inputCantidad.value = 1;
    else if (cantidad > stockDisponible) {
        inputCantidad.value = stockDisponible;
        alert(`⚠️ Solo puedes agregar ${stockDisponible} unidades más.`);
    }
}

function obtenerCantidadEnCarrito(id) {
    const producto = carrito.find(item => item.id === id);
    return producto ? producto.cantidad : 0;
}

function actualizarContadorCarrito() {
    const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
    const totalProductos = carrito.reduce((total, item) => total + item.cantidad, 0);
    const contadorCarrito = document.getElementById("contador-carrito");
    if (contadorCarrito) {
        contadorCarrito.innerText = totalProductos;
    }
}
function agregarAlCarrito(id) {
    const cantidadInput = document.getElementById(`cantidad-${id}`);
    if (!cantidadInput) return;

    let cantidad = parseInt(cantidadInput.value, 10);
    const accesorio = accesoriosCargados.find(a => a.id === id);
    if (!accesorio) {
        alert("❌ Error: accesorio no encontrado");
        return;
    }

    const stockDisponible = accesorio.stock - obtenerCantidadEnCarrito(id);
    if (cantidad > stockDisponible) {
        alert(`⚠️ Solo puedes agregar ${stockDisponible} unidades más.`);
        return;
    }

    let producto = carrito.find(item => item.id === id);
    if (producto) {
        producto.cantidad += cantidad;
    } else {
        carrito.push({ id, nombre: accesorio.nombre, precio: accesorio.precioVenta, cantidad });
    }

    localStorage.setItem("carrito", JSON.stringify(carrito));
    actualizarContadorCarrito();

    // 🔹 Actualiza el stock en la UI
    actualizarStockUI(id);
}

function actualizarStockUI(id) {
    const accesorio = accesoriosCargados.find(a => a.id === id);
    if (!accesorio) return;

    const stockDisponible = accesorio.stock - obtenerCantidadEnCarrito(id);
    const stockElemento = document.querySelector(`#cantidad-${id}`).parentNode.querySelector("p:nth-child(5)");

    if (stockElemento) {
        stockElemento.innerHTML = `<strong>Stock: </strong> ${stockDisponible} unidades`;
    }

    // 🔹 Ajusta el valor máximo del input
    const inputCantidad = document.getElementById(`cantidad-${id}`);
    if (inputCantidad) {
        inputCantidad.max = stockDisponible;
    }
}

function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token
    };
}*/
$(document).ready(function() {
    $("#header").load("headerCarritoEmpleado.html", function() {
        console.log("✅ Header cargado correctamente.");
        actualizarContadorCarrito();
    });
    cargarAccesorios();
});

let accesoriosCargados = [];
let carrito = JSON.parse(localStorage.getItem("carrito")) || [];
let paginaActual = 1;
const accesoriosPorPagina = 6;

async function cargarAccesorios() {
    try {
        const request = await fetch('/api/accesorio', { method: 'GET', headers: getHeaders() });
        accesoriosCargados = await request.json();
        if (!Array.isArray(accesoriosCargados)) throw new Error("Datos de accesorios no válidos");
        mostrarAccesorios();
    } catch (error) {
        console.error("❌ Error al cargar accesorios:", error);
        alert("No se pudieron cargar los accesorios. Inténtalo más tarde.");
    }
}

function mostrarAccesorios() {
    const contenedor = document.getElementById("lista-accesorios");
    if (!contenedor) {
        console.error("❌ No se encontró el contenedor de accesorios");
        return;
    }
    contenedor.innerHTML = "";
    const inicio = (paginaActual - 1) * accesoriosPorPagina;
    const fin = inicio + accesoriosPorPagina;
    const accesoriosPagina = accesoriosCargados.slice(inicio, fin);

    accesoriosPagina.forEach(accesorio => {
        if (!accesorio || typeof accesorio !== 'object') return;
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
    if (!inputCantidad || !accesorio) return;

    const cantidadEnCarrito = obtenerCantidadEnCarrito(id);
    const stockDisponible = accesorio.stock - cantidadEnCarrito;
    let cantidad = parseInt(inputCantidad.value, 10);

    if (isNaN(cantidad) || cantidad < 1) inputCantidad.value = 1;
    else if (cantidad > stockDisponible) {
        inputCantidad.value = stockDisponible;
        alert(`⚠️ Solo puedes agregar ${stockDisponible} unidades más.`);
    }
}

function obtenerCantidadEnCarrito(id) {
    const producto = carrito.find(item => item.id === id);
    return producto ? producto.cantidad : 0;
}

function actualizarContadorCarrito() {
    const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
    const totalProductos = carrito.reduce((total, item) => total + item.cantidad, 0);
    const contadorCarrito = document.getElementById("contador-carrito");
    if (contadorCarrito) {
        contadorCarrito.innerText = totalProductos;
    }
}
function agregarAlCarrito(id) {
    const cantidadInput = document.getElementById(`cantidad-${id}`);
    if (!cantidadInput) return;

    let cantidad = parseInt(cantidadInput.value, 10);
    const accesorio = accesoriosCargados.find(a => a.id === id);
    if (!accesorio) {
        alert("❌ Error: accesorio no encontrado");
        return;
    }

    const stockDisponible = accesorio.stock - obtenerCantidadEnCarrito(id);
    if (cantidad > stockDisponible) {
        alert(`⚠️ Solo puedes agregar ${stockDisponible} unidades más.`);
        return;
    }

    let producto = carrito.find(item => item.id === id);
    if (producto) {
        producto.cantidad += cantidad;
    } else {
        carrito.push({ id, nombre: accesorio.nombre, precio: accesorio.precioVenta, cantidad });
    }

    localStorage.setItem("carrito", JSON.stringify(carrito));
    actualizarContadorCarrito();

    // 🔹 Actualiza el stock en la UI
    actualizarStockUI(id);
}

function actualizarStockUI(id) {
    const accesorio = accesoriosCargados.find(a => a.id === id);
    if (!accesorio) return;

    const stockDisponible = accesorio.stock - obtenerCantidadEnCarrito(id);
    const stockElemento = document.querySelector(`#cantidad-${id}`).parentNode.querySelector("p:nth-child(5)");

    if (stockElemento) {
        stockElemento.innerHTML = `<strong>Stock: </strong> ${stockDisponible} unidades`;
    }

    // 🔹 Ajusta el valor máximo del input
    const inputCantidad = document.getElementById(`cantidad-${id}`);
    if (inputCantidad) {
        inputCantidad.max = stockDisponible;
    }
}

function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token
    };
}