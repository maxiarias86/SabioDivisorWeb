# SabioDivisor – Gestión de Gastos Compartidos

SabioDivisor es una aplicación web desarrollada en Java para gestionar gastos compartidos entre múltiples usuarios. Está pensada para situaciones como compartir gastos en una casa, un viaje, o una cena entre amigos.
Cada usuario puede registrar lo que pagó, repartirlo entre quienes corresponda, ver quién le debe a quién, registrar pagos entre usuarios y proyectar balances a futuro. El sistema mantiene un estado de cuenta actualizado que muestra saldos y deudas entre personas.
Es una migración de la app de escritorio presentada en el segundo parcial, ahora como aplicación web desarrollada con Spring Boot.

## Tecnologías utilizadas

* Java 17
* Spring Boot
* Spring MVC + Thymeleaf
* Spring Data JPA (Hibernate como ORM)
* MySQL como base principal
* HTML + Tailwind CSS
* Maven

## Seguridad

* Login con autenticación basada en sesión.
* Contraseñas protegidas con BCrypt.
* El acceso a los formularios y operaciones está restringido a usuarios logueados.

## ORM utilizado

La aplicación usa JPA para la persistencia de datos. Spring Boot trae Hibernate por defecto como implementación.
El acceso a datos se maneja a través de entidades con `@Entity` y repositorios que extienden `JpaRepository`.

## Funcionalidades principales

* Alta, baja y modificación de gastos.
* División de cada gasto entre uno o más pagadores y deudores.
* Cálculo automático de deudas a partir de los gastos ingresados.
* Registro de pagos entre usuarios (para saldar deudas o prestar dinero).
* Cálculo automático de saldos entre usuarios.
* Visualización de estados de cuenta y deudas a una fecha determinada, pudiendo ser proyectado a una fecha futura (considerando cuotas pendientes).
* Registro, edición, login y logout de usuarios (no se permite eliminar usuarios).

## Diseño y arquitectura

La aplicación sigue una arquitectura en capas, con separación clara de responsabilidades:

* **View**: compuesta por páginas HTML que utilizan `Thymeleaf` para integrar datos dinámicos y formularios interactivos.
* **Controller**: se encarga de recibir las solicitudes del usuario (HTTP), llamar a los Service correspondientes y retornar las vistas o redirecciones necesarias.
* **Service**: contiene la lógica de negocio de la aplicación, como el cálculo de balances, validación de datos, generación de deudas, control de acceso, etc
* **Repository**: maneja el acceso a la base de datos mediante interfaces que extienden JpaRepository, utilizando `Hibernate` como ORM.
* **Model**: incluye las clases que representan entidades del sistema. Cada una está mapeada con @Entity.
* **DTO (Data Transfer Object)**: se usan para comunicar datos entre capas, especialmente entre Controller y Service, sin utilizar las entidades del modelo.

## Clases principales

* **AppUser**: representa a un usuario del sistema, con sus datos básicos (ID, nombre, email, contraseña). Se relaciona con todas las demás clases.
* **Transaction** (abstracta): clase base que representa cualquier tipo de transacción monetaria.
* **Expense**: hereda de `Transaction` y representa un gasto compartido. Cada gasto puede dar origen a una o más deudas (`Debt`), que registran quién le debe a quién, cuánto y en qué fecha vence esa deuda (aplicable a gastos en cuotas).
* **Payment**: también hereda de `Transaction`. Representa un pago directo entre dos usuarios (para saldar deudas o prestar dinero).
* **Debt**: representa una deuda individual generada a partir de un gasto. Indica el acreedor, el deudor, el monto y la fecha de vencimiento. Está asociada a un `Expense`.

**Nota**: a diferencia de la versión de escritorio, en esta aplicación web la clase `Expense` no contiene directamente una lista de `Debt`. En cambio, cada `Debt` tiene un atributo `Expense` que referencia al gasto que le dio origen, y define internamente quién es el acreedor y quién el deudor según el reparto del monto.

## Manejo de validaciones y excepciones

La aplicación cuenta con un sistema de validaciones y manejo de errores para evitar que se rompa el flujo de ejecución, y al mismo tiempo brindar mensajes de error al usuario desde las vistas.

### Validaciones

Las validaciones se realizan principalmente en la capa Service y abarcan:

* Montos inválidos (negativos o nulos).
* Inconsistencias entre el monto total del gasto y lo que pagaron los usuarios.
* Falta de pagadores o deudores.
* Cuotas menores a 1.
* Validaciones adicionales según el caso.

Estas validaciones arrojan una excepción `IllegalArgumentException`.

También se realizan validaciones básicas en el Controller (por ejemplo, que no falten campos obligatorios) para evitar llamados innecesarios al Service y hacer la aplicación más robusta.

### Manejo de excepciones

Las excepciones se capturan en los métodos del Controller mediante bloques `try-catch`. En caso de error:

* Se retorna al formulario.
* Se envía un mensaje con el detalle del error usando model.addAttribute("error", "Descripción del error").
* El mensaje se muestra para que el usuario pueda corregirlo sin perder lo que ya había cargado.

## Usuario de prueba

Para ingresar rápidamente a la aplicación:

* Email: `maxi@gmail.com`
* Contraseña: `1234` (también usada por todos los usuarios de prueba)

También es posible registrarse desde la pantalla de login.

## Casos de uso implementados

CU01 – Registrar usuario: Permite crear una nueva cuenta o editar una ya existente.
CU02 – Iniciar sesión: Verifica las credenciales ingresadas para acceder al sistema.
CU03 – Cerrar sesión: Finaliza la sesión activa del usuario.
CU04 – Cargar gasto: Permite registrar un gasto con fecha, monto, descripción, número de cuotas, pagadores y beneficiarios.
CU05 – Registrar pago: Permite registrar un pago directo entre dos usuarios del sistema.
CU06 – Ver resumen personal: Muestra cuánto debe y cuánto le deben al usuario, tanto de forma global como específica con cada persona.
CU07 – Ver estado de cuenta a una fecha: Permite calcular un balance proyectado teniendo en cuenta deudas actuales y cuotas futuras.

## Cómo correr la aplicación

1. Clonar el repositorio o descomprimir el ZIP.

2. Crear una base de datos MySQL llamada `sabiodivisor`.

   Para facilitar la instalación, se incluye el archivo `sabiodivisor.sql` en la carpeta raíz del repositorio.

3. Configurar las credenciales en el archivo `src/main/resources/application.properties`:

   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/sabiodivisor
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   ```
