package com.cinema.models;

import java.time.LocalDate;

public class Movie {
    private int id;
    private Integer categoryId;
    private String categoryName;
    private String title;
    private String genre;
    private String rating;
    private Integer durationMinutes;
    private LocalDate releaseDate;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    @Override public String toString() {
        return "Movie{id=" + id + ", title='" + title + "', category=" +
                (categoryName != null ? categoryName : categoryId) + ", genre='" + genre +
                "', rating='" + rating + "', duration=" + durationMinutes + "}";
    }
}
