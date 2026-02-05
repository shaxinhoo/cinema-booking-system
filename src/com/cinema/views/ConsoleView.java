package com.cinema.views;

import com.cinema.repositories.MovieRepository;
import com.cinema.repositories.ShowtimeRepository;

public class ConsoleView {
    private final MovieRepository movies = new MovieRepository();
    private final ShowtimeRepository showtimes = new ShowtimeRepository();

    public void start() {
        System.out.println("Cinema Booking (Iteration 2) - Dev1 skeleton");
        System.out.println("Run DB schema in Supabase: db/schema_reset.sql");
        System.out.println("Next commits will add auth/roles and booking flow.\n");

        try {
            System.out.println("Showtimes:");
            showtimes.findAllDetailed().forEach(System.out::println);

            System.out.println("\nMovies:");
            movies.findAll().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
