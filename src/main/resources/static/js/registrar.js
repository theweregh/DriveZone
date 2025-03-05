function getElementValue(id) {
    let element = document.getElementById(id);
    if (!element) {
        alert(`❌ Error: No se encontró el campo con id "${id}". Verifica el HTML.`);
        throw new Error(`Elemento con id "${id}" no encontrado`);
    }
    return element.value.trim();
}

function validarCorreo(email) {
    const regexCorreo = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[cC][oO][mM]$/;
    return regexCorreo.test(email);
}

async function registrarUsuarios() {
    try {
        let datos = {
            username: getElementValue('txtUserName'),
            nombres: getElementValue('txtNombre'),
            cedula: getElementValue('txtCedula'),
            correo: getElementValue('txtCorreo'),
            direccion: getElementValue('txtDireccion'),
            telefono: getElementValue('txtTelefono'),
            password: getElementValue('txtPassword'),
            estado: "Activo",
            rol: "Cliente"
        };

        let repetirPassword = getElementValue('txtRepeatPassword');

        // 🔹 Verificar que no haya campos vacíos
        for (let key in datos) {
            if (datos[key] === "") {
                alert(`⚠ El campo ${key} no puede estar vacío.`);
                return;
            }
        }

        // Validar nombres
        const nombreRegex = /^[A-Za-zñÑáéíóúÁÉÍÓÚ\s]+$/;
        if (!nombreRegex.test(datos.nombres)) {
            alert("❌ Los nombres solo deben contener letras y espacios.");
            return;
        }

        // Validar cédula
        if (datos.cedula.length > 10) {
            alert("❌ La cédula debe tener un máximo de 10 caracteres.");
            return;
        }

        // Validar dirección
        const direccionRegex = /^[A-Za-z0-9\s#\-.\/]+$/;
        if (!direccionRegex.test(datos.direccion)) {
            alert("❌ La dirección contiene caracteres no permitidos.");
            return;
        }

        // Validar teléfono
        const telefonoRegex = /^\d+$/;
        if (!telefonoRegex.test(datos.telefono)) {
            alert("❌ El teléfono solo debe contener números.");
            return;
        }
        if (datos.telefono.length < 10) {
            alert("❌ El teléfono debe tener un mínimo de 10 caracteres.");
            return;
        }

        // Validar correo
        if (!validarCorreo(datos.correo)) {
            alert("⚠ El correo electrónico no tiene un formato válido. Debe contener '@' y terminar en '.com'");
            return;
        }

        // Validar contraseña
        const regexPassword = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&\-_])[A-Za-z\d@$!%*?&\-_]{8,}$/;
        if (!regexPassword.test(datos.password)) {
            alert("⚠ La contraseña debe tener al menos 8 caracteres, incluir una mayúscula, un número y un carácter especial.");
            return;
        }

        // Verificar que las contraseñas coincidan
        if (datos.password !== repetirPassword) {
            alert('⚠ Las contraseñas no coinciden.');
            return;
        }

        // Validar correo de empresa y rol
        const correoEmpresaRegex = /^[a-zA-Z0-9._%+-]+@drivezone\.com$/;
        if (correoEmpresaRegex.test(datos.correo)) {

                alert("❌ ingrese otro dominio de correo");
                return;

        }

        // 🔹 Realizar la petición para registrar el usuario
        const response = await fetch('api/usuarios', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datos)
        });

        // 🔹 Manejo de errores del servidor
        if (!response.ok) {
            const errorMessage = await response.text();
            alert(`❌ Error: ${errorMessage}`);
            return;
        }

        alert('✅ Usuario registrado correctamente');
        window.location.href = 'index.html';
    } catch (error) {
        alert('❌ Hubo un problema con el registro');
        console.error(error);
    }
}