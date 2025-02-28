$(document).ready(function() {
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
    if(respuesta == 'ok'){
        location.href = 'buscarUsuario.html';
    }else{
        alert('Usuario o contraseña incorrectos');
    }
  }