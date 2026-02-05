package com.cinema.controllers;

import com.cinema.repositories.ShowtimeRepository;
import com.cinema.security.AuthorizationService;

public class ShowtimeController {
    private final ShowtimeRepository repo;
    private final AuthorizationService authz;

    public ShowtimeController(ShowtimeRepository repo, AuthorizationService authz) {
        this.repo = repo;
        this.authz = authz;
    }

    public java.util.List<com.cinema.models.Showtime> getShowtimes() throws Exception {
        return repo.findAllDetailed();
    }

    public void listShowtimes() throws Exception {
        repo.findAllDetailed().forEach(System.out::println);
    }
}
