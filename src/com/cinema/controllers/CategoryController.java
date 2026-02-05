package com.cinema.controllers;

import com.cinema.security.AuthorizationService;
import com.cinema.security.Role;
import com.cinema.services.CategoryService;

public class CategoryController {
    private final CategoryService service;
    private final AuthorizationService authz;

    public CategoryController(CategoryService service, AuthorizationService authz) {
        this.service = service;
        this.authz = authz;
    }

    // everyone can read
    public void listCategories() throws Exception {
        service.list().forEach(System.out::println);
    }

    // secured endpoints
    public void createCategory(String name) throws Exception {
        authz.require(Role.ADMIN, Role.MANAGER, Role.EDITOR);
        System.out.println("Created: " + service.create(name));
    }

    public void deleteCategory(int id) throws Exception {
        authz.require(Role.ADMIN);
        System.out.println(service.delete(id) ? "Deleted" : "Not found");
    }
}
