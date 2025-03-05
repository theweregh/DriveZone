document.addEventListener("DOMContentLoaded", async function() {
    const response = await fetch('http://localhost:3000/productos');
    const productos = await response.json();

    productos.forEach(producto => {
        document.getElementById(`nombre${producto.id}`).value = producto.nombre;
        document.getElementById(`descripcion${producto.id}`).value = producto.descripcion;
        document.getElementById(`precio${producto.id}`).value = producto.precio;
        document.getElementById(`cantidad${producto.id}`).value = producto.cantidad;
    });
});

async function actualizarInventario() {
    const productos = [1, 2, 3].map(id => ({
        id,
        nombre: document.getElementById(`nombre${id}`).value,
        descripcion: document.getElementById(`descripcion${id}`).value,
        precio: document.getElementById(`precio${id}`).value,
        cantidad: document.getElementById(`cantidad${id}`).value
    }));
    
    for (let producto of productos) {
        await fetch('http://localhost:3000/productos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(producto)
        });
    }
    alert('Inventario actualizado');
}
