const API_URL = "http://localhost:3000/productos";
// Variable global para almacenar los productos
let productosCargados = [];

document.addEventListener("DOMContentLoaded", () => {
    // Cargar productos al iniciar
    obtenerProductos();

    // Selección de elementos
    const modal = document.getElementById("modal");
    const btnAbrirModal = document.getElementById("btnAbrirModal");
    const btnCerrarModal = document.getElementById("btnCerrarModal");
    const btnGuardarProducto = document.getElementById("btnGuardarProducto");
    const btnActualizarInventario = document.querySelector(".botonActualizarInventario");
    const inputBusqueda = document.getElementById("busquedaInput");

    // Eventos
    btnAbrirModal.addEventListener("click", () => {
        modal.style.display = "flex";
    });

    btnCerrarModal.addEventListener("click", () => {
        modal.style.display = "none";
    });

    btnGuardarProducto.addEventListener("click", agregarProducto);
    btnActualizarInventario.addEventListener("click", actualizarInventario);
    
    // Agregar evento para la búsqueda
    inputBusqueda.addEventListener("input", filtrarProductos);
});

// Obtener productos de la API
async function obtenerProductos() {
    try {
        const response = await fetch(API_URL);
        const data = await response.json();
        console.log("📦 Productos recibidos:", data);
        // Guardar los productos en la variable global
        productosCargados = data;
        mostrarProductos(data);
    } catch (error) {
        console.error("❌ Error obteniendo productos:", error);
    }
}

// Mostrar productos en el HTML
function mostrarProductos(productos) {
    const contenedor = document.querySelector(".productos");
    contenedor.innerHTML = "";
    productos.forEach(producto => agregarProductoAlHTML(producto));
}

// Agregar un producto al HTML
function agregarProductoAlHTML(producto) {
    const contenedor = document.querySelector(".productos");
    const divProducto = document.createElement("div");
    divProducto.classList.add("producto1");
    divProducto.dataset.id = producto.id; // Asigna el id como data attribute

    divProducto.innerHTML = `
        <div class="bordeTabla">
            <input type="text" value="${producto.nombre}" class="textoTitulo">
        </div>
        <textarea class="bordeTabla">${producto.descripcion}</textarea>
        <div class="bordeTabla">
            <input type="number" value="${producto.precio}" class="textoPrecio">
        </div>
        <div class="textoCantidad">
            <input type="number" value="${producto.cantidad}" class="textoCantidadInput">
            
        </div>
        <div class="Eliminar"><button class="btnEliminar"></button></div>
    `;

    // Agregar evento al botón eliminar
    divProducto.querySelector(".btnEliminar").addEventListener("click", () => eliminarProducto(producto.id));

    contenedor.appendChild(divProducto);
}

// Agregar nuevo producto a la API
async function agregarProducto() {
    const nombre = document.getElementById("nombreModal").value.trim();
    const descripcion = document.getElementById("descripcionModal").value.trim();
    const precio = document.getElementById("precioModal").value.trim();
    const cantidad = document.getElementById("cantidadModal").value.trim();

    if (!nombre || !descripcion || !precio || !cantidad) {
        alert("⚠ Todos los campos son obligatorios.");
        return;
    }

    const producto = {
        nombre,
        descripcion,
        precio: Number(precio),
        cantidad: Number(cantidad)
    };

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(producto)
        });
        const data = await response.json();
        console.log("✅ Producto agregado:", data);
        obtenerProductos();
        limpiarFormulario();
        document.getElementById("modal").style.display = "none"; // Cierra el modal
    } catch (error) {
        console.error("❌ Error al agregar producto:", error);
    }
}

// Limpiar los campos del formulario del modal
function limpiarFormulario() {
    document.getElementById("nombreModal").value = "";
    document.getElementById("descripcionModal").value = "";
    document.getElementById("precioModal").value = "";
    document.getElementById("cantidadModal").value = "";
}

// Actualizar productos existentes
async function actualizarInventario() {
    // Selecciona solo los productos reales con data-id
    const productos = document.querySelectorAll(".producto1[data-id]");

    try {
        for (const producto of productos) {
            const id = producto.dataset.id;
            const nombre = producto.querySelector(".textoTitulo").value.trim();
            const descripcion = producto.querySelector("textarea").value.trim();
            const precio = producto.querySelector(".textoPrecio").value.trim();
            const cantidad = producto.querySelector(".textoCantidadInput").value.trim();

            // Validación para evitar valores vacíos
            if (!nombre || !descripcion || !precio || !cantidad) {
                alert(`⚠ El producto con ID ${id} tiene campos vacíos. Revisa los datos.`);
                continue; // O puedes hacer return si quieres detener todo
            }

            const productoActualizado = {
                nombre,
                descripcion,
                precio: Number(precio),
                cantidad: Number(cantidad)
            };

            const response = await fetch(`${API_URL}/${id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(productoActualizado)
            });
            const data = await response.json();
            console.log(`✅ Producto ${id} actualizado:`, data);
        }

        alert("✅ Inventario actualizado correctamente.");
        obtenerProductos(); // Refresca la lista
    } catch (error) {
        console.error("❌ Error al actualizar inventario:", error);
    }
}

// Eliminar producto por ID
async function eliminarProducto(id) {
    const confirmacion = confirm("¿Estás seguro de eliminar este producto?");
    if (!confirmacion) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "DELETE"
        });

        if (response.ok) {
            console.log(`🗑 Producto ${id} eliminado`);
            obtenerProductos(); // Refresca la lista
        } else {
            console.error("❌ Error al eliminar el producto");
        }
    } catch (error) {
        console.error("❌ Error al eliminar producto:", error);
    }
}

function filtrarProductos() {
    const textoBusqueda = document.getElementById("busquedaInput").value.toLowerCase();
    const productosFiltrados = productosCargados.filter(producto =>
        producto.nombre.toLowerCase().includes(textoBusqueda)
    );
    mostrarProductos(productosFiltrados); // Solo se muestra lo que coincide
}