# DWF-Integrantes del equipo:
1. Cristian Alexander Hernández Valiente HV240081
2. Salvador Enrique Delgado Peñate DP240093
3. Mario Antonio Pacheco Guerrero PG240099
4. Alejandra Marisol Hernández Monterrosa HM240103





README del Proyecto
Este proyecto implementa una API REST para la gestión de clientes y pedidos, junto con la funcionalidad de gestión de productos. A continuación, se detalla la estructura general del código, la documentación de la API (utilizando Postman) y las pruebas unitarias realizadas.

# Desarrollo de los controladores
Los controladores (src/main/java/sv/edu/udb/controller) son los puntos de entrada de la API. Se desarrollaron siguiendo el patrón de diseño MVC (Modelo-Vista-Controlador), donde los controladores actúan como intermediarios entre las peticiones HTTP y la lógica de negocio implementada en la capa de servicios.

Responsabilidades Generales:
1.Recepción de Peticiones: Reciben las peticiones HTTP (GET, POST, PUT, DELETE) en endpoints específicos.
2.Deserialización de Datos: Convierten los datos de las peticiones (generalmente en formato JSON) a objetos Java (DTOs o entidades).
3.Validación de Datos: Utilizan anotaciones de Jakarta Validation (@Valid) para asegurar que los datos de entrada cumplan con las reglas definidas.
4.Delegación a Servicios: Invocan los métodos correspondientes en las clases de la capa de servicios (src/main/java/sv/edu/udb/service) para ejecutar la lógica de negocio.
5.Serialización de Respuestas: Convierten los resultados de la capa de servicios a formato JSON para enviarlos como respuesta HTTP.
6.Manejo de Excepciones: Implementan @ExceptionHandler para capturar y manejar excepciones de manera centralizada, devolviendo respuestas HTTP apropiadas (códigos de estado y mensajes de error).

Controladores Específicos:
1.ClienteController: Gestiona las operaciones relacionadas con los clientes (creación, lectura, actualización, eliminación).
2.PedidoController: Gestiona las operaciones relacionadas con los pedidos (creación, lectura por ID y cliente).
3.ProductoController (si existe): Gestiona las operaciones relacionadas con los productos (creación, lectura, actualización, eliminación).


# Documentacion API: Uso de Postman
Para interactuar con la API y explorar sus funcionalidades, se recomienda utilizar Postman. Postman es una herramienta popular para construir, probar y documentar APIs.

Para cada endpoint de la API:

URL Base: La URL base de la API es generalmente http://localhost:8080/api (el puerto 8080 puede variar según tu configuración).
Método HTTP: Especifica el método HTTP correcto (GET, POST, PUT, DELETE).
Endpoint: Añade el endpoint específico (ej., /clientes, /pedidos, /productos).
Headers: Asegúrate de incluir el header Content-Type: application/json para las peticiones con cuerpo JSON (POST, PUT).
Body (para peticiones con datos): Proporciona el cuerpo de la petición en formato JSON según la estructura esperada por el endpoint (ver los ejemplos de JSON en las pruebas).
Verificación de la Respuesta: Analiza el código de estado HTTP y el cuerpo de la respuesta JSON para verificar el resultado de la operación.
Puedes crear colecciones en Postman para organizar las diferentes peticiones de la API y facilitar su prueba y documentación.

A continuación, se listan las pruebas realizadas para el ClienteController como ejemplo, junto con sus resultados esperados (estos resultados se verificarían tanto en las pruebas unitarias como al realizar las peticiones correspondientes con Postman):

1. Pruebas para Cliente:

Crear un nuevo cliente:
Método: POST
Endpoint: /api/clientes
Body (Ejemplo):
JSON:
{
  "nombre": "Laura Gómez",
  "email": "laura.gomez@gmail.com",
  "telefono": "8888-8888",
  "direccion": "Av. Las Flores #789"
}
Resultado Esperado: Código de estado 201 Created y el cuerpo de la respuesta contiene el objeto Cliente creado.

Intentar crear un cliente con email duplicado o un email invalido:
Resultado Esperado: Código de estado 400 "El email ya está registrado." o "agregar un email invalido"

2. Pruebas para Productos:

Crear un nuevo producto:
Método: POST
Endpoint: /api/productos
Body (Ejemplo):
JSON:
{
  "descripcion": "Café negro clásico",
  "precio": 2.50,
  "categoria": "Bebidas",
  "stock": 100,
  "disponible": true
}
Resultado Esperado: Código de estado 201 Created y el cuerpo de la respuesta contiene el objeto producto creado.

Intentar crear un producto sin nombre o un precio negativo:
Resultado Esperado: Código de estado 400 "Debe agregar un nombre al producto." o "El precio debe ser positivo"


3. Pruebas para Pedidos:

Crear un nuevo pedido:
Método: POST
Endpoint: /api/pedidos
Body (Ejemplo):
JSON:
{
  "clienteId": 1,
  "metodoPago": "Tarjeta",
  "estado": "PENDIENTE",
  "productos": [
    {
      "productoId": 1,
      "cantidad": 2
    },
    {
      "productoId": 3,
      "cantidad": 1
    }
  ]
}
Resultado Esperado: Código de estado 201 Created y el cuerpo de la respuesta contiene el objeto pedido creado.

Intentar crear un producto sin stock o un precio negativo:
Resultado Esperado: Código de estado 400 "Producto sin stock." o "El precio debe ser positivo"

Validar el precio de los productos en base a la cantidad:
Resultado Esperado: "En base a la cantidad seleccionada es como que se multiplique el precio del producto por n cantidad seleccionada"
