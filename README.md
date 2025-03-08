# **📌 DriveZone**

🚗 *Sistema de gestión de ventas de accesorios  para vehículos*

## **📖 Descripción**

DriveZone es una plataforma diseñada para gestionar la compra y venta de accesorios  para automóviles. La empresa se especializa en la importación de piezas especiales para la línea de automóviles de la empresa matriz, así como para otras marcas. Cuenta con un local físico donde se venden e instalan los accesorios y también opera una tienda en línea para ampliar su alcance.

La infraestructura de la empresa incluye:

- Un (1) equipo Windows utilizado para la caja registradora.
- Cuatro (4) puntos de red para el personal de la tienda.
- Conexión inalámbrica para empleados, restringida al acceso a Internet mediante autenticación con nombre de usuario.

## **🚀 Tecnologías Usadas**

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA** (Hibernate)
- **MySQL**
- **Lombok**
- **Jakarta Persistence (JPA)**
- **JSON (Jackson)**

## **📂 Estructura del Proyecto**

```
DriveZone/
│── src/main/java/com/DriveZone/DriveZone
│   ├── models/           # Modelos de la base de datos (Entidades JPA)
│   ├── controllers/      # Controladores para manejar las peticiones HTTP
│   ├── services/         # Lógica de negocio y comunicación con la base de datos
│   ├── repositories/     # Interfaces para acceso a datos con Spring Data JPA
│   ├── security/         # Seguridad y autenticación (si aplica)
│   ├── DriveZoneApplication.java  # Punto de entrada del sistema
│── src/main/resources/
│   ├── application.properties  # Configuración de la base de datos y otros parámetros
│── pom.xml                     # Configuración de Maven y dependencias
│── README.md                    # Documentación del proyecto
```

## **⚙️ Instalación y Configuración**

### **1️⃣ Clonar el repositorio**

```bash
git clone https://github.com/TU-USUARIO/DriveZone.git
cd DriveZone
```

### **2️⃣ Configurar la base de datos**

- Asegúrate de tener **MySQL** instalado.
- Modifica el archivo `application.properties` con tus credenciales:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/drivezone
  spring.datasource.username=root
  spring.datasource.password=tu_password
  ```

### **3️⃣ Construir y ejecutar el proyecto**

```bash
mvn clean install
mvn spring-boot:run
```

## **🛠 API Endpoints Principales**

| Método | Endpoint        | Descripción                   |
| ------ | --------------- | ----------------------------- |
| `GET`  | `/usuarios`     | Lista todos los usuarios      |
| `POST` | `/usuarios`     | Crea un nuevo usuario         |
| `GET`  | `/carrito/{id}` | Obtiene un carrito de compras |
| `POST` | `/ordenes`      | Crea una orden de compra      |
| `GET`  | `/accesorios`   | Lista accesorios disponibles  |

## **📌 Funcionalidades**

✅ Gestión de usuarios y roles (ADMINISTRATIVO, EMPLEADO, CLIENTE,ASESORVENTA)\
✅ Carrito de compras con múltiples productos\
✅ Generación de órdenes de compra con impuestos y descuentos\
✅ Registro de búsquedas con logs

## **📜 Licencia**

Este proyecto está bajo la licencia **MIT**.

# DriveZone