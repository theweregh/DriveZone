async function registrarUsuarios() {
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

    // Validar que no haya campos vacíos
    for (let key in datos) {
        if (datos[key] === "") {
            alert(`❌ El campo "${key}" no puede estar vacío.`);
            return;
        }
    }

    // Validar nombres y apellidos (solo letras y espacios, incluyendo ñ y tildes)
    const nombreRegex = /^[A-Za-zñÑáéíóúÁÉÍÓÚ\s]+$/;
    if (!nombreRegex.test(datos.nombres)) {
        alert("❌ Los nombres solo deben contener letras y espacios.");
        return;
    }
    if (!nombreRegex.test(datos.apellidos)) {
        alert("❌ Los apellidos solo deben contener letras y espacios.");
        return;
    }

    // Validar cédula (solo números, máximo 10 dígitos)
    const cedulaRegex = /^\d{1,10}$/;
    if (!cedulaRegex.test(datos.cedula)) {
        alert("❌ La cédula debe contener solo números y tener máximo 10 dígitos.");
        return;
    }

    // Validar dirección (puede contener letras, números, espacios y # - . /)
    const direccionRegex = /^[A-Za-z0-9\s#\-.\/]+$/;
    if (!direccionRegex.test(datos.direccion)) {
        alert("❌ La dirección contiene caracteres no permitidos.");
        return;
    }

    // Validar teléfono (solo números)
    const telefonoRegex = /^\d+$/;
    if (!telefonoRegex.test(datos.telefono)) {
        alert("❌ El teléfono solo debe contener números.");
        return;
    }

    // Validar contraseña (mínimo 8 caracteres, una mayúscula, un número y un carácter especial)
    const passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
    if (!passwordRegex.test(datos.password)) {
        alert("❌ La contraseña debe tener al menos:\n🔹 8 caracteres\n🔹 1 mayúscula\n🔹 1 número\n🔹 1 carácter especial.");
        return;
    }

    // Validar que las contraseñas coincidan
    if (datos.password !== repetirPassword) {
        alert("❌ Las contraseñas no coinciden.");
        return;
    }

    // Validar el correo y determinar el rol
    const correoEmpresaRegex = /^[a-zA-Z0-9._%+-]+@drivezone\.com$/;
    if (correoEmpresaRegex.test(datos.correo)) {
        if (!["Administrativos", "Empleados", "Asesor de ventas"].includes(datos.rol)) {
            alert("❌ Si el usuario es empleado, debe especificar un rol válido: Administrativos, Empleados o Asesor de ventas.");
            return;
        }
    } else {
        datos.rol = "Cliente"; // Cualquier otro correo es un cliente
    }

    alert("✅ Todos los datos son válidos. Enviando información...");

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
            const errorData = await request.json();
            alert(`❌ Error en el registro: ${errorData.mensaje || "Error desconocido"}`);
            return;
        }

        alert("✅ Usuario registrado correctamente.");
        window.location.href = "index.html";
    } catch (error) {
        alert("❌ Hubo un problema con el registro. Revisa la consola para más detalles.");
        console.error(error);
    }
}