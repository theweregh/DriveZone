document.addEventListener("DOMContentLoaded", () => {
    const btnRegistrar = document.getElementById("btnRegistrar");
    if (btnRegistrar) {
        btnRegistrar.addEventListener("click", registrarCliente);
    }
});

async function registrarCliente() {
    let datos = {
        nombre: document.getElementById('txtNombre').value.trim(),
        apellido: document.getElementById('txtApellido').value.trim(),
        cedula: document.getElementById('txtCedula').value.trim(),
        direccion: document.getElementById('txtDireccion').value.trim(),
        telefono: document.getElementById('txtTelefono').value.trim(),
        estado: true
    };

    // Validaciones
    for (let key in datos) {
        if (datos[key] === "") {
            alert(`El campo ${key} no puede estar vacío`);
            return;
        }
    }

    try {
        const request = await fetch('/clientes/registro', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datos)
        });

        if (!request.ok) {
            throw new Error("Error en el registro");
        }

        alert('Cliente registrado correctamente');
        window.location.href = 'login.html';
    } catch (error) {
        alert('Hubo un problema con el registro');
        console.error(error);
    }
}
