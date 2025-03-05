const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');

const app = express();
app.use(express.json());
app.use(cors());

const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'Sebastian1097492723',
    database: 'inventario'
});

db.connect(err => {
    if (err) {
        console.error('Error al conectar a MySQL:', err);
        return;
    }
    console.log('Conectado a MySQL');
});

// Obtener productos
app.get('/productos', (req, res) => {
    db.query('SELECT * FROM productos', (err, results) => {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        res.json(results);
    });
});

app.get("/", (req, res) => {
    res.sendFile(path.join(__dirname, "public", "index.html"));
});


// Actualizar producto
app.post('/productos', (req, res) => {
    const { id, nombre, descripcion, precio, cantidad } = req.body;
    db.query(
        'INSERT INTO productos (id, nombre, descripcion, precio, cantidad) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE nombre = ?, descripcion = ?, precio = ?, cantidad = ?',
        [id, nombre, descripcion, precio, cantidad, nombre, descripcion, precio, cantidad],
        (err) => {
            if (err) {
                res.status(500).json({ error: err.message });
                return;
            }
            res.json({ message: 'Producto actualizado' });
        }
    );
});

app.listen(3000, () => {
    console.log('Servidor corriendo en http://localhost:3000');
});
