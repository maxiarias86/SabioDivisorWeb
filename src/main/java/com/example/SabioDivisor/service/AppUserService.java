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
        // Siempre encripta la contraseña, incluso si ya está encriptada. Por eso debe validar antes de llamar a este metodo.
        String hashedPassword = BCrypt.withDefaults().hashToString(10, appUser.getPassword().toCharArray());
        appUser.setPassword(hashedPassword);
        if (appUser.getId() == null) {//Si el ID es nulo, es un nuevo usuario.
            if (repository.findByEmail(appUser.getEmail()) != null) {// Si ya existe un usuario con ese email, lanza una excepción.
                throw new IllegalArgumentException("Ya existe un usuario con ese email.");// Esto es una validación para evitar mails duplicados.
            }
        }
        return repository.save(appUser);
    }

    /*
    No se van a poder eliminar usuarios.
    public void delete(Long id) {
        repository.deleteById(id);
    }
     */

    public AppUser findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public AppUser findByEmail(String email) {
        return repository.findByEmail(email);
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
