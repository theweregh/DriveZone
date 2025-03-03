package com.DriveZone.DriveZone.Buscar_cliente.Modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "Cliente")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   
        private long idCliente;
        private String nombre;
        private String apellido;
        private String cedula;
        private String direccion;
        private String telefono;
        private boolean estado;
    
        public Cliente(long idCliente, String nombre, String apellido, String cedula, String direccion, String telefono, boolean estado) {
            this.idCliente = idCliente;
            this.nombre = nombre;
            this.apellido = apellido;
            this.cedula = cedula;
            this.direccion = direccion;
            this.telefono = telefono;
            this.estado = estado;
        }
    
    

    public Long getId() { return idCliente; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCedula() { return cedula; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public boolean isEstado() { return estado; }

    public void setId(Long id) { this.idCliente = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEstado(boolean estado) { this.estado = estado; }
}
