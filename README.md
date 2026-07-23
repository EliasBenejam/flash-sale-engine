Flash Sale Engine
Motor de comercio electrónico de alta concurrencia diseñado para gestionar eventos de ventas flash ("Flash Sales") masivas, garantizando consistencia transaccional y previniendo la sobreventa (overselling) mediante una arquitectura desacoplada entre Redis y PostgreSQL.

Arquitectura y Decisiones de Diseño
En escenarios de ventas flash con miles de usuarios concurrentes, las bases de datos relacionales tradicionales colapsan por el bloqueo de tablas y la saturación del pool de conexiones. Este motor resuelve dicho cuello de botella mediante:

Caché Atómica en Memoria (Redis): El stock de los productos se gestiona directamente en la memoria RAM utilizando el bucle monohilo de Redis (DECR), garantizando atomicidad nativa a velocidad de microsegundos sin bloqueos pesados.

Validación de Overselling: Si el decremento atómico en Redis resulta menor a cero, la petición se rechaza de inmediato antes de impactar en la base de datos relacional.

Persistencia Segura (PostgreSQL): Una vez que Redis otorga luz verde, la orden se procesa y consolida de manera segura en PostgreSQL.

Tecnologías Utilizadas
Java 17

Spring Boot (Spring Data JPA, Spring Data WebMVC, Spring Data Redis)

PostgreSQL (Persistencia relacional)

Redis (Control de concurrencia y stock en memoria)

Docker & Docker Compose (Contenedorización de infraestructura)

Configuración y Ejecución Local
1. Levantar Redis en Docker
Ejecuta el siguiente contenedor mapeando un puerto alternativo para evitar conflictos:

Bash
docker run --name flash-redis -p 6380:6379 -d redis:7
2. Configurar Propiedades
Asegúrate de configurar tu archivo src/main/resources/application.properties con tus credenciales de PostgreSQL y el puerto de Redis:

Properties
spring.datasource.url=jdbc:postgresql://localhost:5433/flash_sale_db?options=-c%20timezone=UTC
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.data.redis.host=localhost
spring.data.redis.port=6380

3. Ejecutar la Aplicación
Utiliza el Maven Wrapper incluido en el proyecto:

Bash
./mvnw clean spring-boot:run

API Endpoints
Comprar Producto
URL: /api/flash-sale/buy/{productId}

Método: POST

Parámetros de consulta: userId (Long)

Ejemplo con cURL:

Bash
curl -X POST "http://localhost:8080/api/flash-sale/buy/1?userId=123"
