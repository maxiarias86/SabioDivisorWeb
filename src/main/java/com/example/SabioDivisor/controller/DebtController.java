package com.example.SabioDivisor.controller;

import com.example.SabioDivisor.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/debts")
public class DebtController {

    @Autowired
    private DebtService service;
    public String list(Model model) {
        model.addAttribute("debts", service.findAll());
        return "debts/list"; // Esto apunta a la vista Thymeleaf en src/main/resources/templates/debts/list.html
    }







}
