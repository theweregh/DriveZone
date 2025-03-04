/*$(document).ready(function() {
//on ready
});
async function iniciarSesion(){
let datos = {
    correo: document.getElementById('correo').value,
    password: document.getElementById('password').value,
}
  const request = await fetch('api/login', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
        body: JSON.stringify(datos)
        });
    const respuesta = await request.text();
    if(respuesta != 'error'){
        localStorage.token = respuesta;
        localStorage.email = datos.correo;
        if (datos.correo.endsWith("@drivezone.com")) {
        window.location.href = "buscarUsuario.html";
        } else {
            window.location.href = "CarritoCompra.html";
        }
    }else{
        alert('Usuario o contraseña incorrectos');
    }
  }*/
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
        localStorage.token = respuesta;
        localStorage.email = datos.correo;

        // 🔥 Ahora consultamos el rol del usuario
        const requestUser = await fetch(`api/usuario/${datos.correo}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        });

        const usuario = await requestUser.json();
        console.log("Usuario obtenido:", usuario);
        if (datos.correo.endsWith("@drivezone.com")) {
            if (usuario.rol === "Administrativo") {
            window.location.href = "Administrador.html";
        } else if (usuario.rol === "AsesorVentas") {
            window.location.href = "CarritoCompra.html";
        } else if (usuario.rol === "Empleado") {
            window.location.href = "CarritoCompra.html";
        }
        } else {
            window.location.href = "CarritoCompra.html";
        }/*else {
            alert("Rol no reconocido: " + usuario.rol);
        }*/
    } else {
        alert('Usuario o contraseña incorrectos');
    }
}