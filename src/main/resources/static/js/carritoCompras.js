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
}
document.getElementById("procesar-compra").addEventListener("click", async function () {
    const carrito = obtenerCarritoDesdeLocalStorage();

    if (!carrito.length) {
        alert("⚠️ El carrito está vacío. Agrega productos antes de comprar.");
        return;
    }

    const token = localStorage.getItem("token");
    if (!token) {
        alert("⚠️ Debes iniciar sesión para realizar una compra.");
        return;
    }

    try {
        const usuario = await obtenerUsuario();
        if (!usuario) {
            alert("❌ No se pudo obtener la información del usuario. Inténtalo de nuevo.");
            return;
        }
        const emailCliente = usuario.correo;
        if (!emailCliente) {
        alert("⚠ Debes ingresar un correo electrónico.");
        return;
        }
        console.log("✅ Usuario autenticado ID:", usuario.id, "Rol:", usuario.rol, "📩 Correo:", emailCliente);
        console.log("📩 descuento:", await calcularDescuento(carrito));
        // Construcción de la orden de compra
        const orden = {
            productos: carrito.map(item => ({
                id_accesorio: item.id,
                cantidad: item.cantidad
            })),
            vendedor: "Vendedor no especificado",
            datosEmpresa: "Empresa no especificada",
            fecha: new Date().toISOString().split("T")[0],
            precioVenta: calcularPrecioVenta(carrito),
            subtotal: calcularSubtotal(carrito),
            descuento: await calcularDescuento(carrito),
            impuesto: calcularImpuesto(carrito),
            total: calcularTotal(carrito),
            usuario: usuario,
        };

        // Enviar la orden al backend
        const response = await fetch("api/ordenes", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": token
            },
            body: JSON.stringify(orden)
        });

        if (!response.ok) throw new Error(`Error en la compra (${response.status})`);

        const resultado = await response.json();
        console.log("Respuesta de la API:", resultado);

        // Obtener ID de la orden de compra
        const idOrden = resultado.idOrdenCompra || resultado.id; // Asegurar compatibilidad con nombres de clave
        if (!idOrden) {
            console.error("No se recibió un ID de orden válido:", resultado);
            alert("❌ Error: No se pudo obtener el ID de la orden.");
            return;
        }

        alert("✅ Compra realizada con éxito. ID de orden: " + idOrden);
        for (let item of carrito) {
    const accesorioData = {
    id: {
        idAccesorio: item.id,
        idOrdenCompra: idOrden
    },
    accesorio: item,
    ordenCompra: { idOrdenCompra: idOrden },
    cantidad: item.cantidad
};
    console.log("Orden enviada:", JSON.stringify(orden, null, 2));

    console.log("Enviando accesorio:", JSON.stringify(accesorioData, null, 2));
        const responseAccesorios = await fetch("/api/accesorios-ordenes", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": token
        },
        body: JSON.stringify(accesorioData)
    });

    if (!responseAccesorios.ok) {
        const errorText = await responseAccesorios.text();
        console.error("Error al asociar accesorio:", errorText);
    }
    }
    // Generar la factura con los datos del cliente
        const facturaData = {
            empresaNombre: usuario.nombre || usuario.nombres,
            nit: usuario.cedula || usuario.nit,
            direccion: usuario.direccion,
            metodoPago: "Tarjeta de crédito",
            subtotal: orden.subtotal,
            descuento: orden.descuento,
            impuestos: orden.impuesto,
            total: orden.total,
            ordenCompra: orden
        };

        console.log("Factura a enviar:", facturaData);
        const facturaResponse = await fetch(`/api/facturas/${idOrden}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": token
            },
            body: JSON.stringify(facturaData)
        });

        if (!facturaResponse.ok) {
            console.error("Error al generar factura:", await facturaResponse.text());
        } else {
            alert("✅ Factura generada correctamente.");
            const factura = await facturaResponse.json();  // Obtener la factura creada
    const idFactura = factura.idFactura || factura.id;  // Ajusta según el campo correcto

    if (!idFactura) {
        console.error("❌ No se pudo obtener la ID de la factura.");
    } else {
        alert("✅ Factura generada correctamente.");

        // 📥 Descargar la factura en PDF con la ID correcta
        descargarFactura(idFactura);
        console.log("📨 Enviando factura por correo...");
        await enviarFacturaPorCorreo(idFactura, emailCliente);
    }
        }
        const stockData = carrito.map(item => ({
    id: item.id,   // Asegúrate de que el backend espera "id", no "id_accesorio"
    stock: item.cantidad
}));
        console.log("carrito enviada:", JSON.stringify(stockData, null, 2));
        // Reducir el stock de los accesorios comprados
        const stockResponse = await fetch("/api/accesorios/reducir-stock", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": token
            },
            body: JSON.stringify(stockData)
            });
        if (!stockResponse.ok) {
            console.error("Error al reducir stock:", await stockResponse.text());
        } else {
            console.log("✅ Stock actualizado correctamente.");
        }
        // Limpiar el carrito tras la compra
        localStorage.removeItem("carrito");
        cargarCarrito();
    } catch (error) {
        console.error("❌ Error al procesar la compra:", error.message, error.stack);
        alert("❌ No se pudo completar la compra. Inténtalo de nuevo.");
    }
});

// Función para calcular el precio total de venta sin descuentos
function calcularPrecioVenta(carrito) {
    return carrito.reduce((total, item) => total + (item.precio * item.cantidad), 0);
}

// Función para calcular el subtotal (precio total con descuentos aplicados)
function calcularSubtotal(carrito) {
    return carrito.reduce((total, item) => {
        const precioDescuento = item.descuento ? item.precio * (1 - item.descuento / 100) : item.precio;
        return total + (precioDescuento * item.cantidad);
    }, 0);
}
async function calcularDescuento(carrito) {
    try {
        let totalDescuento = 0;
        for (let item of carrito) {
            const response = await fetch(`/api/accesorio/${item.id}`, {
                method: 'GET',
                headers: getHeaders()
            });
            if (!response.ok) throw new Error(`No se pudo obtener el accesorio ID ${item.id}`);
            const accesorio = await response.json();

            if (accesorio.descuento && accesorio.descuento > 0) {
                const descuento = (accesorio.precioVenta * accesorio.descuento / 100) * item.cantidad;
                totalDescuento += descuento;
                item.descuento = accesorio.descuento; // Guardamos el descuento en el carrito
            }
        }
        return totalDescuento;
    } catch (error) {
        console.error("❌ Error al calcular descuentos:", error);
        return 0;
    }
}

// Función para calcular el impuesto (19% del subtotal con descuentos aplicados)
function calcularImpuesto(carrito) {
    const subtotal = calcularSubtotal(carrito);
    return subtotal * 0.19;
}

// Función para calcular el total a pagar
function calcularTotal(carrito) {
    const subtotal = calcularSubtotal(carrito);
    const impuesto = calcularImpuesto(carrito);
    return subtotal + impuesto;
}
async function obtenerUsuario() {
    const token = localStorage.getItem("token");

    if (!token) {
        console.warn("⚠️ No hay token almacenado. El usuario no está autenticado.");
        return null;
    }

    try {
        let email;

        // Intentar decodificar el token para obtener el correo
        try {
            const payloadBase64 = token.split(".")[1]; // Extrae el payload
            const payloadDecoded = JSON.parse(atob(payloadBase64)); // Decodifica
            email = payloadDecoded.email || payloadDecoded.correo || payloadDecoded.sub; // Intentar diferentes claves
        } catch (error) {
            console.warn("⚠️ No se pudo decodificar el token.");
            return null;
        }

        if (!email) {
            console.error("❌ El token no contiene un correo válido.");
            return null;
        }

        console.log(`🔍 Buscando usuario con correo: ${email}`);

        const url = `api/usuario/${encodeURIComponent(email)}`; // Codificar el correo para evitar errores

        // Hacer la petición al backend
        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            console.error("❌ Error al obtener usuario:", response.statusText);
            return null;
        }

        const usuario = await response.json(); // Convertir respuesta a JSON
        console.log("✅ Usuario obtenido:", usuario);
        return usuario;

    } catch (error) {
        console.error("❌ Error al procesar la solicitud:", error);
        return null;
    }
}

function descargarFactura(idFactura) {
    const url = `/api/facturas/${idFactura}/pdf`;

    fetch(url, {
        method: "GET",
        headers:  getHeaders()
    })
    .then(response => response.blob())
    .then(blob => {
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = `factura_${idFactura}.pdf`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    })
    .catch(error => console.error("❌ Error al descargar la factura:", error));
}
// Función para enviar la factura por correo

async function enviarFacturaPorCorreo(idFactura, emailCliente) {
    console.log("📨 Intentando enviar factura ID:", idFactura, " al correo:", emailCliente);

    const token = localStorage.getItem("token");

    const response = await fetch(`/api/facturas/${idFactura}/enviar-correo?email=${encodeURIComponent(emailCliente)}`, {
        method: "POST",
        headers: { "Authorization": token }
    });

    const responseText = await response.text();
    console.log("📨 Respuesta del servidor:", responseText);

    if (!response.ok) {
        console.error("❌ Error al enviar factura:", responseText);
        alert("❌ No se pudo enviar la factura.");
    } else {
        alert("✅ Factura enviada a ${emailCliente}");
    }
}
function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token
    };
}


