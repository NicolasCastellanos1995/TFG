# Sistema de Gestión de Inventario con FEFO y Slotting Inteligente

## Descripción del proyecto

Este proyecto consiste en el desarrollo de un sistema multiplataforma para la gestión de inventario en almacenes pequeños y medianos.

Actualmente, muchos entornos logísticos utilizan herramientas poco integradas como hojas de cálculo, lo que genera errores, pérdidas por caducidad y falta de control del stock.

La solución propuesta centraliza la gestión del inventario e incorpora lógica logística avanzada como FEFO (First Expired First Out) y priorización por criticidad de ubicación.

---

## Objetivo

Desarrollar un sistema que permita:

* Controlar el inventario en tiempo real
* Registrar movimientos (entrada, salida y transferencia)
* Gestionar stock por lote y fecha de caducidad
* Aplicar automáticamente reglas FEFO
* Priorizar ubicaciones según criticidad
* Detectar productos bajo stock mínimo

---

## Arquitectura del sistema

El sistema sigue una arquitectura cliente-servidor en tres capas:

### Capa de presentación

* Aplicación Android desarrollada en Kotlin
* Aplicación web
* Consumo de API REST mediante HTTP y JSON

### Capa de lógica de negocio

* Backend desarrollado con Spring Boot
* API REST estructurada por módulos
* Autenticación mediante JWT
* Implementación de lógica FEFO y criticidad

### Capa de datos

* Base de datos PostgreSQL
* Persistencia mediante JPA/Hibernate

---

## Clientes del sistema

### Aplicación Android

* Orientada a operarios
* Registro de movimientos
* Consulta de stock

### Aplicación Web

* Orientada a administradores
* Gestión de usuarios y catálogo
* Visualización global del sistema

---

## Flujo del sistema

1. El usuario inicia sesión
2. Se envían credenciales al backend
3. El backend valida y genera un token JWT
4. El cliente realiza peticiones autenticadas
5. El backend procesa la lógica de negocio
6. Se actualiza la base de datos
7. Se devuelve la información al cliente

---

## Funcionalidades principales

* Gestión de productos, categorías y ubicaciones
* Registro de movimientos de inventario
* Control de stock por producto y ubicación
* Gestión de lotes y fechas de caducidad
* Aplicación de lógica FEFO
* Priorización por criticidad
* Alertas de stock mínimo
* Historial de movimientos

---

## Lógica avanzada

### FEFO (First Expired First Out)

Selecciona automáticamente el lote con fecha de caducidad más próxima.

### Criticidad de ubicación

Prioriza ubicaciones según su relevancia logística.

### Gestión dinámica de stock

El stock se actualiza automáticamente en función de los movimientos.

---

## Seguridad

El sistema implementa distintos mecanismos de autenticación según el cliente:

- Aplicación Android:
  - Autenticación mediante JWT
  - Consumo de API REST
 

- Aplicación web:
  - Autenticación basada en sesión mediante Spring Security
  - Login tradicional con formulario
  - Gestión de roles mediante el contexto de seguridad

En ambos casos, el sistema aplica control de acceso basado en roles (Administrador, Supervisor, Operario).

---

## Ejecución del proyecto

### 1. Backend (Docker + Spring Boot)

El backend utiliza Docker para levantar la base de datos PostgreSQL.

#### Levantar la base de datos

```bash
docker compose up -d
```

#### Ejecutar backend

```bash
.\mvnw.cmd spring-boot:run
```

El backend quedará disponible en:

```txt
http://localhost:8080
```

---

### 2. Aplicación Android

#### Ejecución en emulador (recomendado)

Configurar la URL del backend como:

```txt
http://10.0.2.2:8080
```

#### Ejecución en dispositivo real

2.
1. Instalar la aplicacion ubicada en Aplicacionejecutable en tu movil
2. Conectar el móvil a la misma red WiFi que el PC
3. Obtener la IP del PC
4. Configurar en la pantalla de inicio de la app:


http://IP_DEL_PC:8080


Ejemplo:

http://192.168.1.34:8080


---


## Documentación del código

El backend permite la generación de documentación mediante Javadoc.

```bash
javadoc -d doc *.java
```

---

## Valor diferencial

Este sistema no solo almacena información, sino que aplica lógica de negocio:

* Reducción de pérdidas por caducidad
* Automatización de decisiones logísticas
* Arquitectura escalable cliente-servidor
* Uso multiplataforma (Android y web)

---

## Autor

Nicolás Castellanos
