<div align="center">

#  Oh Bike! - E-commerce API

Backend desarrollado para una tienda virtual de bicicletas como proyecto de la materia **Programación N-Capas**.

**Universidad Centroamericana José Simeón Cañas (UCA)**  
Programación N-Capas • Sección 02  
Docente: **Ing. Luisa Arévalo**

![Java](https://img.shields.io/badge/Java-21-red?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-success?style=for-the-badge)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge)
![JWT](https://img.shields.io/badge/JWT-Authentication-orange?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=for-the-badge)

</div>

---

# 📖 Descripción

**Oh Bike!** es una API REST desarrollada con **Spring Boot** que simula el funcionamiento del backend de una tienda de bicicletas en línea.

El sistema fue diseñado siguiendo una **arquitectura N-Capas**, buscando mantener una separación clara de responsabilidades entre la lógica de negocio, el acceso a datos y la exposición de servicios REST.

La aplicación permite administrar el catálogo de productos, autenticación de usuarios, carrito de compras, órdenes, cupones de descuento, métodos de pago y envío, reseñas y listas de deseos, implementando además mecanismos de seguridad mediante JWT y Spring Security.

Más que únicamente cumplir con los requerimientos académicos, el proyecto buscó aproximarse a la estructura utilizada en aplicaciones empresariales reales, aplicando principios de diseño, buenas prácticas y patrones de desarrollo ampliamente utilizados en la industria.

---

# 👥 Equipo de Desarrollo

- Karla Daniela Martínez Ferrer
- César Isaac Tovar Jovel
- Génesis Karina Pérez Henríquez
- Gerson Steven Majano Guandique

---

# 🎯 Objetivos del Proyecto

El objetivo principal fue desarrollar una API REST robusta para un sistema de comercio electrónico que permitiera:

- Gestionar usuarios con diferentes roles.
- Administrar un catálogo de bicicletas y sus variantes.
- Implementar autenticación segura mediante JWT.
- Simular un flujo completo de compra.
- Gestionar órdenes y seguimiento de pedidos.
- Aplicar reglas de negocio para descuentos mediante cupones.
- Utilizar una arquitectura escalable basada en capas.
- Aplicar buenas prácticas de programación orientada a objetos.

---

# 🏛 Arquitectura del Proyecto

El proyecto sigue una arquitectura **N-Capas**.
### ¿Por qué se eligió una arquitectura N-Capas?

Se seleccionó una arquitectura basada en capas porque permite separar claramente las responsabilidades de cada componente del sistema, facilitando el mantenimiento, la reutilización del código y la escalabilidad de la aplicación.

Esta organización permite que cambios en la lógica de negocio no afecten directamente los controladores ni el acceso a datos, reduciendo el acoplamiento entre componentes.

Además, este enfoque facilita la realización de pruebas, el trabajo colaborativo entre los integrantes del equipo y la incorporación de nuevas funcionalidades sin modificar grandes partes del sistema.

```
Cliente
   │
Controllers
   │
Services
   │
Repositories
   │
PostgreSQL
```

Cada capa posee una única responsabilidad:

## Controllers

- Exponen los endpoints REST.
- Validan las solicitudes.
- Devitan lógica de negocio.
- Devuelven respuestas HTTP adecuadas.

## Services

Es la capa más importante del sistema.

Aquí se implementa:

- reglas de negocio
- validaciones
- procesos de checkout
- aplicación de cupones
- actualización de inventario
- creación de órdenes

## Repositories

Utilizando Spring Data JPA, esta capa encapsula completamente el acceso a la base de datos.

Permite:

- consultas
- inserciones
- actualizaciones
- eliminación de registros

sin escribir SQL manual en la mayoría de casos.

---
# 🧩 Principales Tecnologías

El desarrollo de la API se apoyó en un conjunto de tecnologías modernas del ecosistema Java, seleccionadas por su estabilidad, integración y facilidad para construir aplicaciones empresariales.

| Tecnología | Versión | Descripción                                                                                            |
|------------|----------|--------------------------------------------------------------------------------------------------------|
| **Java** | 21 | Lenguaje principal utilizado para el desarrollo de la aplicación.                                      |
| **Spring Boot** | 3.2.5 | Framework principal para la construcción de la API REST.                                               |
| **Spring Data JPA** | Incluido en Spring Boot 3.2.5 | Persistencia de datos y acceso a la base de datos mediante repositorios.                               |
| **Spring Security** | Incluido en Spring Boot 3.2.5 | Implementación de autenticación, autorización y control de acceso basado en roles.                     |
| **JWT (JJWT)** | 0.12.6 | Generación y validación de JSON Web Tokens para autenticación.                                         |
| **PostgreSQL** | Driver JDBC incluido | Sistema gestor de base de datos relacional.                                                            |
| **Spring Validation (Hibernate Validator)** | Incluido en Spring Boot 3.2.5 | Validación de datos de entrada mediante anotaciones como `@NotBlank`, `@Email` y `@Valid`.             |
| **SpringDoc OpenAPI (Swagger)** | 2.5.0 | Generación automática de documentación interactiva para los endpoints de la API.                       |
| **Lombok** | 1.18.30 | Reducción de código repetitivo mediante generación automática de getters, setters, constructores, etc. |
| **Maven** | 3.x | Gestión de dependencias y automatización de la construcción del proyecto.                              |


---

# ⚙️ Decisiones Técnicas

Durante el desarrollo del proyecto se tomaron las siguientes decisiones técnicas:

- Se utilizó Spring Boot 3.2.5 por su integración con Spring Security, Spring Data JPA y facilidad para desarrollar APIs REST.

- Se eligió PostgreSQL como sistema gestor de bases de datos por su robustez, soporte para transacciones y compatibilidad con Spring Data JPA.

- Se implementó autenticación mediante JWT para mantener una arquitectura Stateless y proteger los recursos de la API.

- Se utilizaron DTOs para evitar exponer directamente las entidades de persistencia y controlar la información enviada al cliente.

- Se implementó un Global Exception Handler mediante @ControllerAdvice para centralizar el manejo de errores.

- Se documentó la API utilizando Swagger/OpenAPI para facilitar las pruebas y el consumo de los endpoints.

# 🔐 Seguridad

La seguridad fue implementada utilizando **Spring Security** junto con **JWT (JSON Web Token)**.

El flujo de autenticación funciona de la siguiente manera:

1. El usuario se registra.
2. Inicia sesión.
3. El servidor genera un JWT.
4. El cliente envía el token en cada petición.
5. Spring Security valida el token antes de permitir el acceso.

Los permisos se controlan mediante roles.

## Roles implementados

### 👤 Cliente

Puede:

- consultar productos
- administrar su carrito
- realizar compras
- administrar su wishlist
- escribir reseñas
- consultar sus órdenes
- editar su perfil

### 🛒 Vendedor

Puede:

- administrar sus productos
- administrar promociones
- visualizar productos vendidos
- consultar inventario

### 👨‍💼 Administrador

Puede:

- administrar roles de usuarios
- administrar categorías
- administrar productos
- administrar promociones
- revisar inventario

---

# 🚲 Módulos Implementados

## Gestión de Usuarios

- Registro
- Login
- Edición de perfil
- Administración de usuarios
- Roles

---

## Catálogo

Se implementó un CRUD completo para:

- Categorías
- Productos

Cada producto puede contener múltiples variantes para representar diferentes configuraciones.

Ejemplo:

- talla
- color
- SKU
- stock
- precio unitario

---

## Inventario

El sistema mantiene control del inventario disponible para cada variante.

Durante el proceso de compra:

- se valida disponibilidad
- se descuenta el stock
- se impide comprar cantidades superiores al inventario

También se desarrolló un endpoint para mostrar productos con pocas unidades disponibles.

---

## Carrito

Cada usuario posee un carrito persistente.

Operaciones disponibles:

- Agregar productos
- Modificar cantidades
- Eliminar productos
- Vaciar carrito

---

## Checkout

Durante el checkout el sistema:

- calcula subtotales
- aplica descuentos
- calcula el envío
- genera la orden
- actualiza el inventario

---

## Cupones

Se implementó soporte para distintos tipos de cupones:

- Descuento porcentual
- Descuento fijo
- Envío gratis

Cada cupón valida:

- fecha de expiración
- cantidad máxima de usos
- disponibilidad

---

## Órdenes

Las órdenes mantienen un flujo de estados.

```
PENDING
      │
      ▼
PAID
      │
      ▼
SHIPPED
```

Esto permite realizar el seguimiento del pedido desde su creación hasta el envío.

---

## Métodos de Pago

Se implementó un catálogo configurable de métodos de pago.

Ejemplo:

- Tarjeta
- Efectivo
- Criptomonedas

---

## Métodos de Envío

El usuario puede seleccionar un método de envío disponible durante el checkout.

---

## Wishlist

Cada cliente puede:

- agregar favoritos
- eliminarlos
- consultarlos posteriormente

---

## Reviews

Los clientes pueden publicar reseñas únicamente después de haber realizado una compra del producto correspondiente.

---

# ⚠ Manejo Global de Errores

La API implementa un manejador global utilizando `@ControllerAdvice`.

Esto permite devolver respuestas consistentes para errores como:

- recurso inexistente
- datos inválidos
- autenticación fallida
- autorización insuficiente
- stock insuficiente
- cupones inválidos

---

# 📑 Documentación de la API

La documentación fue generada utilizando **Swagger/OpenAPI**, permitiendo explorar todos los endpoints disponibles, visualizar los modelos de datos y realizar pruebas directamente desde el navegador.

---

# 📂 Estructura del Proyecto

```
src
 ├── config
 ├── controllers
 ├── dto
 │
 ├── entities
 │
 ├── exceptions
 │
 ├── mappers
 │
 ├── repositories
 │
 ├── security
 │
 ├── services
 │
 └── utils
```

---

# 🚀 Instalación

## Requisitos

- Java 21
- Maven
- PostgreSQL

---

## 1. Clonar repositorio

```bash
git clone <url-del-repositorio>
```

---

## 2. Crear la base de datos

```sql
CREATE DATABASE ohbike;
```

---

## 3. Configurar application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ohbike
spring.datasource.username=postgres
spring.datasource.password=tu_password
```

---

## 4. Ejecutar

```bash
mvn spring-boot:run
```

---

# 📌 Estado del Proyecto

Entre las características desarrolladas se incluyen:

- Autenticación con JWT.
- Gestión de usuarios y roles.
- CRUD de categorías y productos.
- Variantes de productos.
- Carrito persistente.
- Checkout.
- Cupones.
- Métodos de pago.
- Métodos de envío.
- Gestión de órdenes.
- Wishlist.
- Reviews.
- Manejo global de excepciones.
- Documentación mediante Swagger.

---


# 📚 Buenas Prácticas Aplicadas

- Arquitectura N-Capas
- DTO Pattern
- Mapper Pattern
- Repository Pattern
- Dependency Injection
- Global Exception Handler
- Validaciones con Bean Validation
- RESTful API
- Principio de Responsabilidad Única (SRP)
- Separación de responsabilidades

---

<div align="center">

**Oh Bike! 🚲**

Proyecto desarrollado como parte de la asignatura **Programación N-Capas**.

