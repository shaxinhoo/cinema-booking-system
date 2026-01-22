package com.cinema.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Showtime {
    private int id;
    private int movieId;
    private LocalDate showDate;
    private LocalTime showTime;
    private BigDecimal price;
    private int availableSeats;
    private String movieTitle;

    public Showtime() {}

    public Showtime(int movieId, LocalDate showDate, LocalTime showTime,
                    BigDecimal price, int availableSeats) {
        this.movieId = movieId;
        this.showDate = showDate;
        this.showTime = showTime;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public LocalDate getShowDate() { return showDate; }
    public void setShowDate(LocalDate showDate) { this.showDate = showDate; }

    public LocalTime getShowTime() { return showTime; }
    public void setShowTime(LocalTime showTime) { this.showTime = showTime; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s %s | Price: %.0f KZT | Seats: %d",
                id, movieTitle, showDate, showTime, price, availableSeats);
    }
}