# Notas
# Pendientes
* Agregar validaciones en el Service que lance excepciones que son catcheadas en el Controller y mostradas en la vista.

## Dónde me quedé
* Falta el mensaje de error en el expenses/form.html
* No se cargan aquellos gastos en los que no halla habido Debts. Si hay tiempo modificar el Service para que cargue los gastos aunque no haya Debts. Esto es porque al cargar un gasto se crea un Debt por cada debtor y creditor, pero si no hay Debts, no se carga el gasto.
* Funciona mal el editar Gasto. Se carga la mitad del monto en lugar del total.