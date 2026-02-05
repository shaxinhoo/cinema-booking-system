package com.cinema.controllers;

import com.cinema.repositories.MovieRepository;
import com.cinema.security.AuthorizationService;

public class MovieController {
    private final MovieRepository repo;
    private final AuthorizationService authz;

    public MovieController(MovieRepository repo, AuthorizationService authz) {
        this.repo = repo;
        this.authz = authz;
    }

    public java.util.List<com.cinema.models.Movie> getMovies() throws Exception {
        return repo.findAll();
    }

    public void listMovies() throws Exception {
        repo.findAll().forEach(System.out::println);
    }
}
