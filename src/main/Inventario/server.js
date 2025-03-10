const express = require("express");
const mysql = require("mysql2");
const cors = require("cors");
const bodyParser = require("body-parser");

const app = express();

app.use(cors());
app.use(bodyParser.json());

const db = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "",  
    database: "inventario_db",
    port: 3306
});


db.connect(err => {
    if (err) {
        console.error("❌ Error conectando a MySQL:", err);
        return;
    }
    console.log("✅ Conectado a MySQL (XAMPP)");
});

const path = require("path");

// Servir archivos estáticos desde la carpeta 'public'
app.use(express.static(path.join(__dirname)));

app.get("/", (req, res) => {
    res.sendFile(path.join(__dirname, "index.html"));
});

app.get("/productos", (req, res) => {
    db.query("SELECT * FROM productos", (err, result) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(result);
    });
});

// Agregar un nuevo producto al inventario
app.post("/productos", (req, res) => {
    const { nombre, descripcion, precio, cantidad } = req.body;
    const sql = "INSERT INTO productos (nombre, descripcion, precio, cantidad) VALUES (?, ?, ?, ?)";
    
    db.query(sql, [nombre, descripcion, precio, cantidad], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json({ message: "Producto agregado con éxito", id: result.insertId });
    });
});

app.put("/productos/:id", (req, res) => {
    const { id } = req.params;
    const { nombre, descripcion, precio, cantidad } = req.body;

    if (!nombre || !descripcion || !precio || !cantidad) {
        return res.status(400).json({ error: "Todos los campos son obligatorios" });
    }

    const sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, cantidad = ? WHERE id = ?";
    db.query(sql, [nombre, descripcion, precio, cantidad, id], (err, result) => {
        if (err) {
            console.error(`❌ Error al actualizar producto ${id}:`, err);
            return res.status(500).json({ error: "Error al actualizar producto" });
        }

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: "Producto no encontrado" });
        }

        res.json({ id, nombre, descripcion, precio, cantidad });
    });
});

// Iniciar el servidor en el puerto 3000
app.listen(3000, () => {
    console.log("🚀 Servidor corriendo en http://localhost:3000");
});