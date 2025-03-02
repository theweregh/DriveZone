package com.DriveZone.DriveZone.Buscar_cliente.Modelo;
public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private String identificacion;
    private String direccion;
    private String telefono;
    private boolean estado;

    public Cliente() {
    }


    public Cliente(int id, String nombre, String apellido, String identificacion, String direccion, String telefono, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", nombre='" + nombre + '\'' + ", apellido='" + apellido + '\'' + ", identificacion='" + identificacion + '\'' + ", direccion='" + direccion + '\'' + ", telefono='" + telefono + '\'' + ", estado=" + estado + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }


    public void setEstado(String estado) {
        this.estado = Boolean.parseBoolean(estado);
    }

    

    
}
