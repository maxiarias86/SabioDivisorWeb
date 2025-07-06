package com.example.SabioDivisor.service;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository repository;

    public List<AppUser> listAll() {
        return repository.findAll();
    }

    public AppUser save(AppUser appUser) {
        return repository.save(appUser);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public AppUser findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
