## Manejo de Validaciones y Excepciones
La aplicación implementa un sistema de validaciones y manejo de errores pensado para evitar que fallos lógicos rompan el flujo de ejecución, y al mismo tiempo brindar retroalimentación clara al usuario desde la interfaz web.

### Validaciones
Las validaciones se realizan principalmente en la capa Service y abarcan:
* Montos inválidos (negativos o nulos).
* Inconsistencias entre el monto total del gasto y lo que pagaron los usuarios.
* Falta de pagadores o deudores.
* Cuotas menores a 1.
Estas validaciones se expresan mediante IllegalArgumentException que se lanzan si la entrada no cumple con los requisitos.

### Manejo de errores en el Frontend
Las excepciones se capturan desde los métodos del Controller mediante bloques try-catch.
En caso de error:
* Se retorna al formulario.
* Se envía un mensaje con el detalle del error - model.addAttribute("error", "mensaje_de_error") -.
* El mensaje se muestra de forma visible en la interfaz, permitiendo al usuario corregirlo sin perder los datos ingresados.