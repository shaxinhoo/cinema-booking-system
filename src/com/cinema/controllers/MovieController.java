package com.cinema.controllers;

import com.cinema.models.Movie;
import com.cinema.repositories.MovieRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MovieController {
    private MovieRepository movieRepository;

    public MovieController() {
        this.movieRepository = new MovieRepository();
    }

    public Movie addMovie(String title, String description, int duration,
                          String genre, String rating, LocalDate releaseDate, String director) {
        try {
            Movie movie = new Movie(title, description, duration, genre, rating, releaseDate, director);
            return movieRepository.create(movie);
        } catch (SQLException e) {
            System.out.println("Error creating movie: " + e.getMessage());
            return null;
        }
    }

    public Movie getMovieById(int id) {
        try {
            return movieRepository.findById(id);
        } catch (SQLException e) {
            System.out.println("Error finding movie: " + e.getMessage());
            return null;
        }
    }

    public List<Movie> getAllMovies() {
        try {
            return movieRepository.findAll();
        } catch (SQLException e) {
            System.out.println("Error loading movies: " + e.getMessage());
            return null;
        }
    }

    public List<Movie> searchMovies(String keyword) {
        try {
            return movieRepository.searchByTitle(keyword);
        } catch (SQLException e) {
            System.out.println("Error searching movies: " + e.getMessage());
            return null;
        }
    }

    public boolean updateMovie(int id, String title, String description, int duration,
                               String genre, String rating, LocalDate releaseDate, String director) {
        try {
            Movie movie = new Movie(title, description, duration, genre, rating, releaseDate, director);
            movie.setId(id);
            movieRepository.update(movie);
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating movie: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMovie(int id) {
        try {
            movieRepository.delete(id);
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting movie: " + e.getMessage());
            return false;
        }
    }
}
