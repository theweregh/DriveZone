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
            <input type="number" id="cantidad-${accesorio.id}" min="1" value="1">
            <button onclick="agregarAlCarrito(${accesorio.id})">Agregar</button>
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
        'Authorization': localStorage.token || ''
    };
}

// 🔹 Agregar accesorio al carrito (corregido)
async function agregarAlCarrito(id) {
    const cantidad = parseInt(document.getElementById(`cantidad-${id}`).value);
    if (cantidad <= 0) {
        alert("❌ La cantidad debe ser mayor a 0.");
        return;
    }

    // 🔹 Buscar el accesorio en la lista cargada
    const accesorio = accesoriosCargados.find(a => a.id === id);
    if (!accesorio) {
        alert("❌ Error: accesorio no encontrado");
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
}