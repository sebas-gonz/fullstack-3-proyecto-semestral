#  Guía de Instalación y Ejecución del Backend (Sistema ERP)

Este documento detalla los pasos necesarios para levantar el entorno de desarrollo del backend, el cual está construido con una arquitectura de microservicios (Spring Boot), comunicados mediante eventos (Apache Kafka) y orquestados con Eureka y un API Gateway.

##  Pre-requisitos
Asegúrate de tener instalado lo siguiente en tu sistema antes de comenzar:
* **Java JDK 17** (o superior).
* **Maven** (3.8+).
* **Docker Desktop** (o Docker Engine + Docker Compose) en ejecución.

---

## Paso 1: Levantar la Infraestructura (Docker)
El proyecto depende de servicios externos como la base de datos y el broker de mensajería (Apache Kafka). Estos se encuentran configurados en el archivo `docker-compose.yml` en la raíz del proyecto.

1. Abre una terminal en la raíz del proyecto (donde está el archivo `docker-compose.yml`).
2. Ejecuta el siguiente comando para descargar las imágenes y levantar los contenedores en segundo plano:
   `docker-compose up -d`
3. Espera unos segundos y verifica que los contenedores (Kafka, Zookeeper, Base de Datos, etc.) estén en estado **"Running"** o **"Up"** usando el comando:
   `docker ps`

---

##  Paso 2: Limpiar y Compilar el Proyecto
Antes de ejecutar los microservicios, es recomendable limpiar construcciones anteriores y descargar las dependencias.

1. Abre una terminal en la carpeta raíz del `backend/`.
2. Ejecuta el comando global de Maven para limpiar todo el proyecto:
   `mvn clean`

---

## Paso 3: Orden Estricto de Ejecución
Debido a la naturaleza de la arquitectura, **el orden de encendido es fundamental**. Si un microservicio intenta registrarse antes de que el servidor de descubrimiento exista, fallará.

Puedes ejecutar cada microservicio desde tu IDE (IntelliJ IDEA / Eclipse) dándole "Play" a la clase principal, o desde la terminal entrando a cada carpeta y usando `mvn spring-boot:run`.

Sigue estrictamente este orden:

### 1. Servidor de Descubrimiento (Eureka)
* **Proyecto:** `ms-eureka`
* **Acción:** Inícialo y espera a que la consola indique que ha arrancado.
* **Verificación:** Abre tu navegador en `http://localhost:8761`. Deberías ver el panel de Eureka (vacío por ahora).

### 2. Enrutador y Seguridad (API Gateway)
* **Proyecto:** `api-gateway`
* **Acción:** Inícialo. Este servicio se registrará en Eureka y expondrá el puerto `8091` para recibir todas las peticiones del Frontend.

### 3. Microservicios Core (El orden entre estos no importa)
Inicia los siguientes microservicios. Todos se conectarán a su base de datos, establecerán conexión con Kafka y se registrarán en Eureka:
* `ms-usuario`
* `ms-catalogo`
* `msinventario`
* `mspedido`
* `msenvio`

### 4. Backend For Frontend (BFF)
* **Proyecto:** `ms-bff-web`
* **Acción:** Inícialo al final. Este servicio actúa como intermediario para optimizar las respuestas complejas que solicita el Frontend.

---

## ✅ Paso 4: Verificación Final del Ecosistema
1. Vuelve a abrir el panel de Eureka en tu navegador: `http://localhost:8761`.
2. En la sección **"Instances currently registered with Eureka"**,se deberian ver listados todos los microservicios (GATEWAY, MS-USUARIO, MS-INVENTARIO, MS-BFF-WEB, etc.) con el status **UP**.

El backend ya está completamente operativo y listo para recibir peticiones del frontend a través del API Gateway (`http://localhost:8091`)!