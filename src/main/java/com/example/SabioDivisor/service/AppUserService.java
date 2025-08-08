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
    private AppUserRepository appUserRepository;

    public List<AppUser> listAll() {
        return appUserRepository.findAll();
    }

    public AppUser save(AppUser appUser) {
        if (appUser == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (appUser.getPassword() == null || appUser.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o tener espacios.");
        }
        if (appUser.getEmail() == null || appUser.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo o tener espacios.");
        }

        // Siempre encripta la contraseña, incluso si ya está encriptada. Por eso debe validar antes de llamar a este metodo.
        String hashedPassword = BCrypt.withDefaults().hashToString(10, appUser.getPassword().toCharArray());
        appUser.setPassword(hashedPassword);
        if (appUser.getId() == null) {//Si el ID es nulo, es un nuevo usuario.
            if (appUserRepository.findByEmail(appUser.getEmail()) != null) {// Si ya existe un usuario con ese email, lanza una excepción.
                throw new IllegalArgumentException("Ya existe un usuario con ese email.");// Esto es una validación para evitar mails duplicados.
            }
        }
        AppUser u = appUserRepository.findByEmail(appUser.getEmail());
        if (u != null && !u.getId().equals(appUser.getId())) {
                throw  new IllegalArgumentException("Ya existe un usuario con ese email.");
            }

        return appUserRepository.save(appUser);
    }

    public AppUser findById(Long id) {
        return appUserRepository.findById(id).orElse(null);
    }

    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public AppUser validateCredentials(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Email y contraseña no pueden ser nulos o estar en blanco.");
        }
        AppUser u = findByEmail(email);
        if (u != null) {
            if (BCrypt.verifyer().verify(password.toCharArray(), u.getPassword()).verified) {//BCrypt.verifyer().verify() me devuelve un objeto que contiene un atributo de tipo boolean llamado "verified" que indica si la contraseña es correcta o no.
                return u;
            }
        }
        return null;
    }
/*
Cuando el usuario envía el formulario de login, se ejecuta el metodo del LoginController, que llama a AppUserService.validateCredentials().
Se busca el usuario por email y se valida la contraseña con BCrypt. Si es correcta, se guarda el objeto AppUser en la sesión con session.setAttribute("loggedUser", user). Luego, en cualquier controlador que requiera autenticación, recupero ese usuario con session.getAttribute("loggedUser"). Si no hay usuario logueado, redirijo a la pantalla de login.
 */


}
