package com.cinema.controllers;

import com.cinema.security.SecurityContext;
import com.cinema.services.AuthService;

public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) { this.auth = auth; }

    public void register(String email, String pass, String fn, String ln) throws Exception {
        auth.registerCustomer(email, pass, fn, ln);
        System.out.println("Registered. Now login.");
    }

    public void login(String email, String pass) throws Exception {
        var u = auth.login(email, pass);
        if (u == null) { System.out.println("Wrong credentials"); return; }
        SecurityContext.setCurrentUser(u);
        System.out.println("Logged in: " + u.fullName() + " (" + u.getRole() + ")");
    }

    public void logout() {
        SecurityContext.clear();
        System.out.println("Logged out.");
    }
}
