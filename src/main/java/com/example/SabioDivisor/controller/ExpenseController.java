package com.example.SabioDivisor.controller;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.model.Expense;
import com.example.SabioDivisor.service.AppUserService;
import com.example.SabioDivisor.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private AppUserService appUserService;

    @GetMapping
    public String list(Model model, HttpSession session) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {//Si el usuario no esta logueado vuelve al login
            return "redirect:/login";
        }
        model.addAttribute("expenses",expenseService.findAllByUser(loggedUser));
        return "expenses/list";
    }

    @GetMapping("/new")
    public String newExpense(Model model, HttpSession session) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        //Agrega los Maps para los pagadores y deudores, porque el html los espera para mostrar los campos de pago y deuda en el caso de que sea edición.
        model.addAttribute("payers", new HashMap<Long, Double>());
        model.addAttribute("debtors", new HashMap<Long, Double>());

        model.addAttribute("expense", new Expense());//Crea un nuevo objeto Expense para que el formulario pueda llenarlo.
        model.addAttribute("users",appUserService.listAll());
        return "expenses/form";
    }

    @PostMapping("/save")//Será ejecutado cuando se realice una solicitud HTTP POST a la URL "/expenses/save"
    //En este caso, el formulario es templates/expenses/form.html, y al enviarse con method="post" y th:action="@{/save}", llega a este metodo.
    public String saveExpense(//Devuelve un String porque es el nombre de la vista a mostrar después de guardar el gasto.
            @ModelAttribute Expense expense, //Anotación para que Spring llene un objeto Expense con los valores del formulario. Spring busca en el name="..." de cada campo del formulario y llama automáticamente los setters del modelo.
            Model model,//Es el contenedor de datos que se enviarán a la vista. model.addAttribute("expense", expense); Agrega el objeto Expense al modelo para que esté disponible en la vista. Se le puede pasar mensajes de error, éxito, etc.
            HttpSession session,//HttpSession es una interfaz estándar de Java que permite acceder a la sesión del usuario actual.
            @RequestParam Map<String, String> params) {//Captura todos los parámetros del formulario como un Map key-value {"amount":"25000","payer_1":"20000","payer_2:"5000"}.
        /*
        A través del atributo name, Spring vincula los campos del formulario a los atributos del modelo con @ModelAttribute.
         Si hay inputs adicionales que no pertenecen al modelo, como los pagadores o deudores, los capturo con @RequestParam Map<String, String> y los separo por key-value.
         */

        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";//Redirige, ejecuta una nueva solicitud GET a la URL "/login" si el usuario no está logueado.
        }

        //VALIDACIONES: Si el gasto le falta algo o es inválido, se muestra un mensaje de error y se vuelve al formulario.
        if (expense.getDate() == null || expense.getDate().isAfter(LocalDate.now())) {
            model.addAttribute("error", "La fecha del gasto no puede ser nula o futura.");
            model.addAttribute("expense", expense);//Se vuelven a agregar los atributos del expense antes del volver al form
            model.addAttribute("users", appUserService.listAll());//Para que el formulario pueda volver a mostrar los datos de usuarios registrados.
            return "expenses/form";
        }

        if (expense.getAmount() <= 0) {
            model.addAttribute("error", "El monto del gasto debe ser mayor a cero.");
            model.addAttribute("expense", expense);
            model.addAttribute("users", appUserService.listAll());
            return "expenses/form";
        }

        if (expense.getInstallments() < 1 || expense.getInstallments() > 24) {//Se limita a 24 cuotas para evitar deudas eternas.
            model.addAttribute("error", "Las cuotas no pueden ser menores a 1 o mayores a 24.");
            model.addAttribute("expense", expense);
            model.addAttribute("users", appUserService.listAll());
            return "expenses/form";
        }

        Map<Long,Double> payers = new HashMap<>();//Va a contener el id del pagador y el monto que pago. Ej.: {1L:20000, 2L:5000}
        Map<Long,Double> debtors = new HashMap<>();
        /*
        El código JS del front crea muchos campos de formulario con nombres que empiezan con "payer_" o "debtor_". Ej.: <input type="hidden" name="debtor_5" value="900.0">
        El metodo recibe esos parámetros como un Map<String, String> params, donde la clave es el nombre del campo y el valor es el valor del campo.
        Ej: {
          "amount": "2400.0",
          "description": "Alquiler",
          "payer_2": "1500.0",
          "payer_3": "900.0",
          "debtor_2": "1200.0",
          "debtor_4": "1200.0"
            }
            Entonces a continuación se recorre el Map params para buscar los pagadores y deudores, y se guardan en los Maps payers y debtors.
            if (key.startsWith("payer_")) -> Si la clave empieza con "payer_", significa que es un pagador.
            Long id = Long.parseLong(key.substring(6)); -> Se obtiene el id del pagador, que es lo que sigue al sexto caracter de la clave (después de "payer_").
         */
        try{
            for (String key : params.keySet()) {//Recorre todos los parámetros del formulario para buscar claves que empiecen con: "payer_": representa un pagador. "debtor_": representa un deudor.
                if (key.startsWith("payer_")) {//Metodo nativo de Java que devuelve true si la cadena empieza con el prefijo indicado.
                    Long id = Long.parseLong(key.substring(6));//El id es lo que le sigue al sexto caracter (luego de "payer_")
                    Double amount = Double.parseDouble(params.get(key));//Obtiene el monto que pago
                    if (amount > 0) {
                        payers.put(id, amount);//Guarda el id y el monto en el Map payers
                    }
                }
                if (key.startsWith("debtor_")) {
                    Long id = Long.parseLong(key.substring(7));
                    Double amount = Double.parseDouble(params.get(key));
                    if (amount > 0) {
                        debtors.put(id, amount);
                    }
                }
            }
            if(payers.isEmpty() || debtors.isEmpty()) {//Si no hay pagadores o deudores, se muestra un mensaje de error y se vuelve al formulario.
                model.addAttribute("error", "Debe haber al menos un pagador y un deudor.");
                model.addAttribute("expense", expense);
                model.addAttribute("users", appUserService.listAll());
                return "expenses/form";
            }
            if( expense.getAmount() != payers.values().stream().mapToDouble(Double::doubleValue).sum()) {//Si el monto del gasto no coincide con la suma de los pagos, se muestra un mensaje de error y se vuelve al formulario.
                /*
                payers.values() obtiene todos los values del Map payers y los mete en una colección [1000, 4000, 3000].
                .stream() convierte esa colección en un Stream, que es una secuencia de elementos que se pueden procesar.
                .mapToDouble(Double::doubleValue) convierte cada elemento Double del Stream a un double (dato primitivo, no objeto Double) para poder aplicarle el .sum().
                 */
                model.addAttribute("error", "El monto del gasto no coincide con la suma de los pagos.");
                model.addAttribute("expense", expense);
                model.addAttribute("users", appUserService.listAll());
                return "expenses/form";
            }

            if(!payers.containsKey(loggedUser.getId()) && !debtors.containsKey(loggedUser.getId())) {
                model.addAttribute("error", "No puedes agregar gastos en los cuales no participes.");
                model.addAttribute("expense", expense);
                model.addAttribute("users", appUserService.listAll());
                return "expenses/form";
            }

            expenseService.save(expense,payers,debtors);
            return "redirect:/expenses";
        } catch (Exception e) {//Si se lanza una excepción, se muestra el formulario otra vez con: el error, el gasto cargado previamente, la lista de usuarios para volver a seleccionar pagadores/deudores.
            model.addAttribute("error", e.getMessage());
            model.addAttribute("expense", expense);
            model.addAttribute("users", appUserService.listAll());
            model.addAttribute("payers", payers);
            model.addAttribute("debtors", debtors);
            return "expenses/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editExpense(Model model, HttpSession session, @PathVariable Long id) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        Expense expense = expenseService.findById(id);

        //VALIDACIONES: Si el gasto no existe o si el usuario no participó en el gasto, se muestra un mensaje de error y se redirige a la lista de gastos.
        if (expense == null) {
            model.addAttribute("error", "Gasto no encontrado.");
            model.addAttribute("expenses", expenseService.findAllByUser(loggedUser));
            return "expenses/list";
        }
        try{
            if(!expenseService.userParticipatedInExpense(loggedUser, expense.getId())) {
                model.addAttribute("error", "No puedes editar un gasto en el que no participaste.");
                model.addAttribute("expenses", expenseService.findAllByUser(loggedUser));
                return "expenses/list";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("expenses", expenseService.findAllByUser(loggedUser));
            return "expenses/list";
        }


        List<AppUser> users = appUserService.listAll();
        Map<Long, String> userIdToName = new HashMap<>();

        for (AppUser user : users) {
            userIdToName.put(user.getId(), user.getName());
        }

        model.addAttribute("users", users);
        model.addAttribute("userIdToName", userIdToName);
        model.addAttribute("users", users);
        model.addAttribute("expense", expense);

        return "expenses/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        AppUser loggedUser = (AppUser) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        Expense expense = expenseService.findById(id);

        //VALIDACIONES: Si el gasto no existe o si el usuario no participó en el gasto, se muestra un mensaje de error y se redirige a la lista de gastos.
        if (expense == null) {
            redirectAttributes.addFlashAttribute("error", "Gasto no encontrado.");
            return "redirect:/expenses";
        }
        /*
            Flash attributes permiten pasar atributos (datos con clave-valor) durante un redirect. El model.addAttribute(...) no funciona porque los atributos se pierden al redirigir.
            Se usan con redirectAttributes.addFlashAttribute(...) y están disponibles solo en la siguiente solicitud.
        */

        try {
            if(!expenseService.userParticipatedInExpense(loggedUser, expense.getId())) {
                redirectAttributes.addFlashAttribute("error", "No puedes eliminar un gasto en el que no participaste.");
                return "redirect:/expenses";
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/expenses";
        }
        //CONTINUA EL FLUJO

        expenseService.delete(expense.getId());
        redirectAttributes.addFlashAttribute("success", "Gasto eliminado correctamente.");//redirect.Attributes.addFlashAttribute: Guarda temporalmente un atributo (ej. "error") y lo pasa al siguiente redirect. Es "flash" porque se elimina después de que se muestra una vez.
        return "redirect:/expenses";
    }

}
