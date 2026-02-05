package com.cinema.services;

import com.cinema.models.Category;
import com.cinema.repositories.CategoryRepository;
import com.cinema.validation.Validator;

import java.util.List;

public class CategoryService {
    private final CategoryRepository repo = new CategoryRepository();

    public List<Category> list() throws Exception {
        return repo.findAll();
    }

    public Category create(String name) throws Exception {
        new Validator<String>()
                .rule(s -> s != null && s.trim().length() >= 2, "Category name too short")
                .validate(name);
        return repo.create(name.trim());
    }

    public boolean delete(int id) throws Exception {
        new Validator<Integer>().rule(x -> x > 0, "Invalid category id").validate(id);
        return repo.delete(id);
    }
}
