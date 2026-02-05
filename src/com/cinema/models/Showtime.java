package com.cinema.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Showtime {
    private int id;
    private LocalDate showDate;
    private LocalTime showTime;
    private BigDecimal basePrice;
    private String format;
    private int movieId;
    private int hallId;
    private int cinemaId;

    private String movieTitle;
    private String cinemaName;
    private String hallName;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public int getHallId() { return hallId; }
    public void setHallId(int hallId) { this.hallId = hallId; }

    public int getCinemaId() { return cinemaId; }
    public void setCinemaId(int cinemaId) { this.cinemaId = cinemaId; }

    public LocalDate getShowDate() { return showDate; }
    public void setShowDate(LocalDate showDate) { this.showDate = showDate; }

    public LocalTime getShowTime() { return showTime; }
    public void setShowTime(LocalTime showTime) { this.showTime = showTime; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getCinemaName() { return cinemaName; }
    public void setCinemaName(String cinemaName) { this.cinemaName = cinemaName; }

    public String getHallName() { return hallName; }
    public void setHallName(String hallName) { this.hallName = hallName; }

    @Override public String toString() {
        return "Showtime{id=" + id + ", date=" + showDate + ", time=" + showTime +
                ", price=" + basePrice + ", format=" + format +
                ", movie='" + movieTitle + "', cinema='" + cinemaName + "', hall='" + hallName + "'}";
    }
}
