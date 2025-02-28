$(document).ready(function() {
//on ready
});
async function registrarUsuarios(){
let datos = {
    username: document.getElementById('txtUserName').value,
    nombres: document.getElementById('txtNombre').value,
    cedula: document.getElementById('txtCedula').value,
    correo: document.getElementById('txtCorreo').value,
    direccion: document.getElementById('txtDirreccion').value,
    telefono: document.getElementById('txtTelefono').value,
    password: document.getElementById('txtPassword').value,
}
let repetirPassword = document.getElementById('txtRepeatPassword').value;

if(datos.password != repetirPassword){
    alert('Las contraseñas no coinciden');
    return;
}
  const request = await fetch('api/usuarios', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
        body: JSON.stringify(datos)
        });
        alert('Usuario registrado correctamente');
        windows.location.href = 'login.html'
  }