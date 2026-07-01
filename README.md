<div align="center">

# 🚲 Oh Bike! - E-commerce API

Backend desarrollado para una tienda virtual de bicicletas como proyecto de la materia **Programación N-Capas**.

**Universidad Centroamericana José Simeón Cañas (UCA)**  
Programación N-Capas • Sección 02  
Docente: **Ing. Luisa Arévalo**

![Java](https://img.shields.io/badge/Java-17-red?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-success?style=for-the-badge)
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

- Karla Martínez
- Isaac
- Steven
- Karina

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

El proyecto sigue una arquitectura **Layered Architecture (N-Capas)**.

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

| Tecnología          | Uso |
|---------------------|-----|
| Java 21              | Lenguaje principal |
| Spring Boot         | Framework Backend |
| Spring Data JPA     | Persistencia |
| PostgreSQL          | Base de datos |
| Spring Security     | Seguridad |
| JWT                 | Autenticación |
| Hibernate Validator | Validaciones |
| Maven               | Gestión de dependencias |
| Swagger/OpenAPI     | Documentación de la API |

---

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

- Java 17
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

