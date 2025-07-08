package com.example.SabioDivisor.service;

import com.example.SabioDivisor.model.Debt;
import com.example.SabioDivisor.repository.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtService {

    @Autowired
    private DebtRepository repository;

    public List<Debt> findAll() {return repository.findAll();}

    public Debt save(Debt debt) {return repository.save(debt);}

    public void delete(Long id) {repository.deleteById(id);}

    public Debt findById(Long id) {return repository.findById(id).orElse(null);}
}
