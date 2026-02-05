package com.cinema.services;

import com.cinema.models.User;
import com.cinema.repositories.UserRepository;
import com.cinema.validation.Validator;

public class AuthService {
    private final UserRepository users = new UserRepository();
    private final PasswordHasher hasher = new PasswordHasher();

    public User registerCustomer(String email, String password, String fn, String ln) throws Exception {
        new Validator<String>().rule(s -> s != null && s.contains("@"), "Invalid email").validate(email);
        new Validator<String>().rule(s -> s != null && s.length() >= 6, "Password >= 6").validate(password);
        new Validator<String>().rule(s -> s != null && s.length() >= 2, "First name too short").validate(fn);
        new Validator<String>().rule(s -> s != null && s.length() >= 2, "Last name too short").validate(ln);

        if (users.findByEmail(email) != null) throw new IllegalArgumentException("Email already exists");
        return users.createCustomer(email, hasher.sha256(password), fn, ln);
    }

    public User login(String email, String password) throws Exception {
        User u = users.findByEmail(email);
        if (u == null) return null;
        return hasher.sha256(password).equals(u.getPasswordHash()) ? u : null;
    }
}
