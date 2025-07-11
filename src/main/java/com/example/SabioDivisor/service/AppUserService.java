package com.example.SabioDivisor.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
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
        String hashedPassword = BCrypt.withDefaults().hashToString(10, appUser.getPassword().toCharArray());
        appUser.setPassword(hashedPassword);
        return repository.save(appUser);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public AppUser findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public AppUser findByEmail(String email) {
        List<AppUser> users = repository.findAll();
        for (AppUser u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public AppUser validateCredentials(String email, String password) {
        AppUser u = findByEmail(email);
        if (u != null) {
            if (BCrypt.verifyer().verify(password.toCharArray(), u.getPassword()).verified) {
                return u;
            }
        }
        return null;
    }




    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.verifyer()
                .verify(plainPassword.toCharArray(), hashedPassword)
                .verified;
    }


}
