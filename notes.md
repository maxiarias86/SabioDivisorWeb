# Notas
# Pendientes
* Agregar validaciones en el Service que lance excepciones que son catcheadas en el Controller y mostradas en la vista.

## Dónde me quedé
* Se puede hacer pagos futuros. Agregar una validación que no permita hacer pagos futuros. Comprobar que tampoco se puedan cargar gastos futuros.
* Se pueden cargar gastos donde el monto de los deudores es distinto al monto del gasto. Agregar una validación que no permita cargar gastos donde el monto de los deudores o de los acreedores es distinto al monto del gasto.
* Falta el mensaje de error en el expenses/form.html
* No se cargan aquellos gastos en los que no halla habido Debts. Si hay tiempo modificar el Service para que cargue los gastos aunque no haya Debts. Esto es porque al cargar un gasto se crea un Debt por cada debtor y creditor, pero si no hay Debts, no se carga el gasto.
* Funciona mal el editar Gasto. Se carga la mitad del monto en lugar del total.