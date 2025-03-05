CREATE DATABASE inventarioPI;
USE inventarioPI;

CREATE TABLE productos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    descripcion TEXT,
    precio DECIMAL(10,2),
    cantidad INT
);

INSERT INTO productos (nombre, descripcion, precio, cantidad) VALUES
('Producto1', 'Descripción de producto 1', 10.50, 13),
('Producto2', 'Descripción de producto 2', 20.00, 22),
('Producto3', 'Descripción de producto 3', 30.75, 33);
