$(document).ready(function() {
    cargarAccesorios();
});

let accesoriosCargados = [];
let paginaActual = 1;
const accesoriosPorPagina = 6; // Ajusta según el diseño

async function cargarAccesorios() {
    const request = await fetch('api/accesorio', {
        method: 'GET',
        headers: getHeaders()
    });

    accesoriosCargados = await request.json();
    mostrarAccesorios();
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
            <input type="number" id="cantidad-${accesorio.id}" min="1" value="1">
            <button onclick="agregarAlCarrito(${accesorio.id}, '${accesorio.nombre}', ${accesorio.precioVenta})">Agregar</button>
        `;
        contenedor.appendChild(item);
    });

    document.getElementById("pagina-actual").innerText = paginaActual;
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
        'Authorization': localStorage.token
    };
}

// 🔹 Función de carrito (ajústala si la necesitas)
function agregarAlCarrito(id, nombre, precio) {
    alert(`Añadido: ${nombre} - $${precio}`);
}