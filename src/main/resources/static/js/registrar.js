$(document).ready(function() {
    // Esperar que el documento esté listo
});

async function registrarUsuarios() {
    if(document.getElementById('txtPassword').value.length < 8){
        alert('La contraseña debe tener al menos 8 caracteres');

    }
    let datos = {
        username: document.getElementById('txtUserName').value.trim(),
        nombres: document.getElementById('txtNombre').value.trim(),
        cedula: document.getElementById('txtCedula').value.trim(),
        correo: document.getElementById('txtCorreo').value.trim(),
        direccion: document.getElementById('txtDireccion').value.trim(),
        telefono: document.getElementById('txtTelefono').value.trim(),
        password: document.getElementById('txtPassword').value,
        estado: document.getElementById('selectEstado').value,
        rol: document.getElementById('selectRol').value
    };

    let repetirPassword = document.getElementById('txtRepeatPassword').value;

    // Validaciones
    for (let key in datos) {
        if (datos[key] === "") {
            alert(`El campo ${key} no puede estar vacío`);
            return;
        }
    }

    if (datos.password !== repetirPassword) {
        alert('Las contraseñas no coinciden');
        return;
    }

    try {
        const request = await fetch('api/usuarios', {
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

        alert('Usuario registrado correctamente');
        window.location.href = 'index.html';
    } catch (error) {
        alert('Hubo un problema con el registro');
        console.error(error);
    }
}