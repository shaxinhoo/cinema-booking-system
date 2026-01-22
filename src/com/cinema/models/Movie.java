package com.cinema.models;

import java.time.LocalDate;

public class Movie {
    private int id;
    private String title;
    private String description;
    private int durationMinutes;
    private String genre;
    private String rating;
    private LocalDate releaseDate;
    private String director;

    public Movie() {}

    public Movie(String title, String description, int durationMinutes,
                 String genre, String rating, LocalDate releaseDate, String director) {
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.director = director;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%d min) - %s | Rating: %s | Director: %s",
                id, title, durationMinutes, genre, rating, director);
    }
}