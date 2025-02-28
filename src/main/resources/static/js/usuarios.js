$(document).ready(function() {
    cargarUsuarios();
   $('#tablaUsuarios').DataTable(); // Verifica que DataTable esté cargado
//    //cargarUsuarios(); // Llamamos a la función para cargar los datos
});
async function cargarUsuarios(){
  const request = await fetch('api/usuarios', {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
  });
  const usuarios = await request.json();
  console.log(usuarios);
    let listadoHtml = '';

    for (let usuario of usuarios){
        let botomEliminar = '<td><button class="btn btn-danger" onclick="eliminarUsuario('+usuario.id+')">Eliminar</button></td>';
        let usuarioHtml ='<tr><td>'+usuario.id+'</td><td>'+usuario.username+'</td><td>'+usuario.nombres+'</td><td>'+usuario.cedula+'</td><td>'+usuario.correo+'</td><td>'+usuario.direccion+'</td><td>'+usuario.telefono+'</td><td>'+usuario.password+botomEliminar+'</td></tr>';
        //let usuarioHtml = '<tr><td>${usuario.id}</td><td>${usuario.username}</td><td>${usuario.nombres}</td><td>${usuario.cedula}</td><td>${usuario.correo}</td><td>${usuario.direccion}</td> <td>${usuario.telefono}</td></tr>';
        listadoHtml += usuarioHtml;
    }
    document.querySelector('#tablaUsuarios').innerHTML = listadoHtml;
  //document.querySelector( '#tablaUsuarios tbody').outerHTML = listadoHtml;
  //document.querySelector('#tablaUsuarios').innerHTML = listadoHtml;
  }
async function eliminarUsuario(id){
    console.log('Eliminar usuario con id: '+id);
    if(!confirm('¿Está seguro de dar de baja el usuario?')){
        return;
    }
    await fetch('api/usuarios/'+id, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });
    cargarUsuarios();
    location.reaload();
}