package com.example.SabioDivisor.service;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.model.Debt;
import com.example.SabioDivisor.model.Expense;
import com.example.SabioDivisor.repository.AppUserRepository;
import com.example.SabioDivisor.repository.DebtRepository;
import com.example.SabioDivisor.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private AppUserRepository userRepository;

    public Expense findById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Transactional//Actua de forma similar al commit false de los DAO. Si no se completa toda la transaccion a la base de datos no se carga la Expense (Por ejemplo si hay un error en la carga de las Debt).
    public Expense save(Expense expense, Map<Long, Double> payers, Map<Long, Double> debtors) {

        //VALIDACIONES: Se arrojan en el Service y se catchean en el Controller.
        if (expense.getDate() == null || expense.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del gasto no puede ser nula o futura.");
        }
        if (expense.getAmount() <= 0) {
            throw new IllegalArgumentException("El monto del gasto debe ser mayor a cero.");
        }
        if(payers == null || payers.isEmpty()){
            throw new IllegalArgumentException("Debe haber, al menos, un pagador.");
        }
        if(debtors == null || debtors.isEmpty()){
            throw new IllegalArgumentException("Debe haber, al menos, un deudor.");
        }
        if(expense.getInstallments()<1 || expense.getInstallments() > 24){//Se limita a 24 cuotas para evitar deudas eternas.
            throw new IllegalArgumentException("Las cuotas no pueden ser menores a 1 o mayores a 24.");
        }

        Double totalPayed = (double) 0;
        Double totalDebt = (double) 0;
        for (Double value : payers.values()) {
            totalPayed += value;
        }
        for (Double value : debtors.values()) {
            totalDebt += value;
        }
        if (Math.abs(totalPayed - expense.getAmount()) > 0.01 || Math.abs(totalDebt - expense.getAmount()) > 0.01){//Se deja un mínimo margen de error por redondeo.
            throw new IllegalArgumentException("La suma de los pagos o la suma de las deudas es distinta al monto del gasto.");
        }

        Expense savedExpense = expenseRepository.save(expense);//Guarda el Expense y devuelve el objeto guardado con el ID generado que luego va a relacionar a las Debt.
        debtRepository.deleteByExpenseId(savedExpense.getId());//Elimina las deudas asociadas al gasto si es una edición, sino la query delete no va a hacer nada.

        Map<Long, AppUser> users = new HashMap<>();//Armado de un HashMap con todos los usuarios que participan.
        //Este paso junta todos los AppUser que participan del gasto y los guarda en un Map<Long, AppUser> para poder acceder a ellos por ID después.
        for (Long id : payers.keySet()) {//Agregación de los pagadores
            AppUser user = userRepository.findById(id).orElse(null);
            if (user == null) {
                throw new IllegalArgumentException("Pagador con ID " + id + " no existe.");
            }
            users.put(id, user);
        }
        for (Long id: debtors.keySet()) {//Agregacción de los deudores
            if(!users.containsKey(id)) {
                AppUser user = userRepository.findById(id).orElse(null);
                if (user == null) {
                    throw new IllegalArgumentException("Deudor con ID " + id + " no existe.");
                }
                users.put(id, user);
            }
        }

        Map<Long, Double> balances = new HashMap<>();//Creación de un balance.
        for (Long id : payers.keySet()) {//suma lo que pago cada uno
            balances.put(id, payers.get(id));
        }
        for (Long id : debtors.keySet()) {
            Double amountDebt = debtors.get(id);
            if(balances.containsKey(id)) {
                Double balance = balances.get(id) - amountDebt;//Resta la deuda a lo que había pagado
                balances.put(id, balance);
            } else {
                balances.put(id, -amountDebt);//Lo agrega en negativo porque es una deuda
            }
        }

        List<AppUser> creditorList = new ArrayList<>();//Aquellos con saldo positivo
        List<AppUser> debtorList = new ArrayList<>();//Aquellos con saldo negativo

        for (Long id : balances.keySet()) {
            Double amount = balances.get(id);
            AppUser user = users.get(id);//No lo llamo del AppUserRepository porque el Map users es como un cache dentro del metodo.

            if (amount < 0) {
                debtorList.add(user);
            } else if (amount > 0) {//Saltea a aquellos que pagarno justo lo que debían, es decir saldo 0.
                creditorList.add(user);
            }
        }

        List<Debt> debtList = new ArrayList<>();//se genera una lista de deudas distribuyendo los saldos entre acreedores y deudores, con las cuotas correspondientes.
        int installments = expense.getInstallments();

        for (int i = 0 ; i < debtorList.size(); i++) {//Itera sobre el ArrayList de deudores
            AppUser debtor = debtorList.get(i);//Obtiene el objeto AppUser
            Long debtorId = debtor.getId();//Obtiene el ID de ese AppUser que va a usar como clave del Map balances
            Double balance = - balances.get(debtorId);//Obtiene el monto de que debe (el "valor" de la "clave" debtorId en el Map balances)

            for (int j=0 ; j<creditorList.size(); j++ ) {
                AppUser creditor = creditorList.get(j);
                Long creditorId = creditor.getId();
                Double credit = balances.get(creditorId);

                if (balance <=0) {//El deudor ya pagó lo que debía, no hace falta seguir recorriendo acreedores para este deudor.
                    break;//Se corta el for "j" y se pasa al siguiente deudor.
                }
                if (credit <= 0) {//Este acreedor ya recuperó lo que le debían, entonces no se le asigna más deuda.
                    continue;//Salta al siguiente acreedor.
                }

                Double amount = Math.min(balance,credit);//elige el menor de los dos valores positivos.
                Double installmentAmount = amount / installments;

                for (int k = 1 ; k <= installments; k++){
                    LocalDate date = expense.getDate().plusMonths(k - 1);//En cada iteración (número de cuota) cambia la fecha de vencimiento.
                    Debt debt = new Debt(installmentAmount,debtor,creditor,savedExpense,date,k);//Crea la nueva Debt
                    debtList.add(debt);//La agrega a la lista de Debts.
                }

                balances.put(creditorId, credit - amount);
                balance -= amount;
            }
        }
        debtRepository.saveAll(debtList);//Guarda todas las Debt generadas.
        /*
        Si algo falla dado el uso de @Transactional, si alguna linea lanza una excepcion no se guarda ni el Expense ni las Debt.
        La base de datos queda como estaba. Es como el conn.rollback() de la app de escritorio (DAO).
        */
    return savedExpense;
    }

    public List<Expense> findAllByUser(AppUser user) {
        if (user == null || user.getId() == null) {
            return new ArrayList<>(); // Retorna una lista vacía si el usuario es nulo o no tiene ID
        }
        return expenseRepository.findAllByUserId(user.getId());
    }

    public void delete(Long id) {
        debtRepository.deleteByExpenseId(id);//Primero borra las Debt asociadas.
        expenseRepository.deleteById(id);//Borra el Expense
    }

    public boolean userParticipatedInExpense(AppUser user, Long expenseId) {
        if (user == null || user.getId() == null) {
            return false; // Retorna false si el usuario es nulo o no tiene ID
        }
        return debtRepository.countByExpenseId(expenseId, user.getId()) > 0;
    }

}
