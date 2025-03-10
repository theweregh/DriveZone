// Cargar productos al iniciar
document.addEventListener("DOMContentLoaded", () => {
    obtenerProductos(); 

    // Selección de elementos
    const modal = document.getElementById("modal");
    const btnAbrirModal = document.getElementById("btnAbrirModal");
    const btnCerrarModal = document.getElementById("btnCerrarModal");
    const btnGuardarProducto = document.getElementById("btnGuardarProducto");
    const btnActualizarInventario = document.querySelector(".botonActualizarInventario");

    // Abrir modal solo cuando se presiona el botón
    btnAbrirModal.addEventListener("click", () => {
        modal.style.display = "flex";
    });

    // Cerrar modal al presionar la "X"
    btnCerrarModal.addEventListener("click", () => {
        modal.style.display = "none";
    });

    // Guardar producto cuando se presiona el botón
    btnGuardarProducto.addEventListener("click", () => {
        agregarProducto();
    });

    // Actualizar inventario cuando se presiona el botón
    btnActualizarInventario.addEventListener("click", () => {
        actualizarInventario();
    });
});

// unción para obtener productos desde la base de datos

function obtenerProductos() {
    fetch("http://localhost:3000/productos")
        .then(response => response.json())
        .then(data => {
            console.log("📦 Productos recibidos:", data);
            mostrarProductos(data);
        })
        .catch(error => console.error("❌ Error obteniendo productos:", error));
}

// Función para mostrar productos en la interfaz

function mostrarProductos(productos) {
    const contenedor = document.querySelector(".productos");
    contenedor.innerHTML = "";

    productos.forEach((producto) => {
        agregarProductoAlHTML(producto);
    });
}

//  Función para agregar productos con su ID en la interfaz

function agregarProductoAlHTML(producto) {
    const contenedor = document.querySelector(".productos");

    const divProducto = document.createElement("div");
    divProducto.classList.add("producto1");
    divProducto.setAttribute("data-id", producto.id); 

    divProducto.innerHTML = `
        <div class="texto"><input type="text" value="${producto.nombre}" class="textoTitulo"></div>
        <textarea class="texto">${producto.descripcion}</textarea>
        <div class="texto"><input type="number" value="${producto.precio}" class="texto"></div>
        <div class="texto"><input type="number" value="${producto.cantidad}" class="texto"></div>
    `;

    contenedor.appendChild(divProducto);
}

// Función para agregar un nuevo producto a la base de datos

function agregarProducto() {
    const nombre = document.getElementById("nombreModal").value.trim();
    const descripcion = document.getElementById("descripcionModal").value.trim();
    const precio = document.getElementById("precioModal").value.trim();
    const cantidad = document.getElementById("cantidadModal").value.trim();

    if (!nombre || !descripcion || !precio || !cantidad) {
        alert("⚠ Todos los campos son obligatorios.");
        return;
    }

    const producto = {
        nombre: nombre,
        descripcion: descripcion,
        precio: Number(precio),
        cantidad: Number(cantidad)
    };

    fetch("http://localhost:3000/productos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(producto)
    })
    .then(response => response.json())
    .then(data => {
        console.log("✅ Producto agregado:", data);
        obtenerProductos(); 
    })
    .catch(error => console.error("❌ Error al agregar producto:", error));
}

// Función para actualizar los productos en la base de datos

function actualizarInventario() {
    const productos = document.querySelectorAll(".producto1");

    productos.forEach(producto => {
        const id = producto.getAttribute("data-id"); 
        const nombre = producto.querySelector(".textoTitulo").value.trim();
        const descripcion = producto.querySelector("textarea").value.trim();
        const precio = producto.querySelector("input[type='number']").value.trim();
        const cantidad = producto.querySelectorAll("input[type='number']")[1].value.trim();

        const productoActualizado = {
            id: Number(id),
            nombre: nombre,
            descripcion: descripcion,
            precio: Number(precio),
            cantidad: Number(cantidad)
        };

        fetch(`http://localhost:3000/productos/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(productoActualizado)
        })
        .then(response => response.json())
        .then(data => {
            console.log(`✅ Producto ${id} actualizado`, data);
        })
        .catch(error => console.error(`❌ Error al actualizar producto ${id}:`, error));
    });

    alert("Inventario actualizado correctamente.");
}
