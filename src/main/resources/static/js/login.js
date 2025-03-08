async function iniciarSesion() {
    let datos = {
        correo: document.getElementById('correo').value,
        password: document.getElementById('password').value,
    };

    const request = await fetch('api/login', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(datos)
    });

    const respuesta = await request.text();

    if (respuesta !== 'error') {
        // Guardamos el token
        localStorage.token = respuesta;
        localStorage.email = datos.correo;

        // 🔹 Ahora consultamos el rol del usuario con el token en los headers
        const requestUser = await fetch(`api/usuario/${datos.correo}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': respuesta // 🔥 Enviar el token en la cabecera
            }
        });

        if (!requestUser.ok) {
            alert("Error al obtener el usuario. Sesión expirada o no autorizado.");
            window.location.href = "login.html"; // Redirigir si el token no es válido
            return;
        }

        const usuario = await requestUser.json();
        console.log("Usuario obtenido:", usuario);

        // 🔥 Verificar el rol y redirigir
        if (datos.correo.endsWith("@drivezone.com")) {
            if (usuario.rol === "Administrativo") {
                window.location.href = "Administrador.html";
            } else if (usuario.rol === "AsesorVentas" || usuario.rol === "Empleado") {
                window.location.href = "CarritoCompra.html";
            }
        } else {
            window.location.href = "CarritoCompra.html";
        }
    } else {
        alert('Usuario o contraseña incorrectos');
    }
}