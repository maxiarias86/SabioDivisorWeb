# SabioDivisor – Gestión de Gastos Compartidos
Aplicación web pensada para compartir gastos, por ejemplo en una casa, en un viaje o en cualquier otro proyecto grupal.  
Cada usuario puede cargar lo que pagó, repartirlo entre quienes corresponda, ver quién le debe a quién, registrar pagos entre usuarios y proyectar balances a futuro.  
Es una migración de la app de escritorio presentada en el segundo parcial, ahora como aplicación web desarrollada con Spring Boot.

## Tecnologías Utilizadas
- Java 17
- Spring Boot
- Spring MVC + Thymeleaf
- Spring Data JPA (Hibernate como ORM)
- MySQL como base principal
- HTML + Tailwind CSS
- Maven

## Seguridad
- Login con autenticación basada en sesión.
- Contraseñas protegidas con BCrypt.
- El acceso a los formularios y operaciones está restringido a usuarios logueados.

## ORM utilizado
La aplicación usa JPA para la persistencia de datos. Spring Boot trae Hibernate por defecto como implementación, así que no fue necesario configurarlo explícitamente.  
Toda la lógica de acceso a datos se maneja a través de entidades con `@Entity` y repositorios que extienden `JpaRepository`.

## Funcionalidades principales
- Alta, baja y modificación de gastos.
- División de cada gasto entre uno o más pagadores y deudores.
- Registro de pagos entre usuarios (para saldar deudas o prestar dinero).
- Cálculo automático de saldos entre usuarios.
- Visualización de estados de cuenta y deudas a una fecha determinada.
- Gestión de usuarios registrados.

## Manejo de Validaciones y Excepciones
La aplicación implementa un sistema de validaciones y manejo de errores pensado para evitar que fallos lógicos rompan el flujo de ejecución, y al mismo tiempo brindar retroalimentación clara al usuario desde la interfaz web.

### Validaciones
Las validaciones se realizan principalmente en la capa Service y abarcan:
- Montos inválidos (negativos o nulos).
- Inconsistencias entre el monto total del gasto y lo que pagaron los usuarios.
- Falta de pagadores o deudores.
- Cuotas menores a 1.

Estas validaciones lanzan `IllegalArgumentException` cuando la entrada no cumple los requisitos.

### Manejo de errores en el Frontend

Las excepciones se capturan en los métodos del Controller mediante bloques `try-catch`. En caso de error:

- Se retorna al formulario.
- Se envía un mensaje con el detalle del error usando `model.addAttribute("error", "mensaje_de_error")`.
- El mensaje se muestra en la interfaz para que el usuario pueda corregirlo sin perder lo que ya había cargado.

## Usuario de prueba

Para ingresar rápidamente a la aplicación:
- Email: `maxi@gmail.com`
- Contraseña: `1234`

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

   Para facilitar la instalación, se incluye el archivo `sabiodivisor.sql` en el repositorio. Podés importarlo con:

   ```
   mysql -u tu_usuario -p sabiodivisor < sabiodivisor.sql
   ```

   Luego, configurar las credenciales en el archivo `src/main/resources/application.properties`:

   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/sabiodivisor
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   ```
