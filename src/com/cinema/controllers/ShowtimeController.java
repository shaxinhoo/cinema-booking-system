package com.cinema.controllers;

import com.cinema.models.Showtime;
import com.cinema.repositories.ShowtimeRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ShowtimeController {
    private ShowtimeRepository showtimeRepository;

    public ShowtimeController() {
        this.showtimeRepository = new ShowtimeRepository();
    }

    public Showtime addShowtime(int movieId, LocalDate date, LocalTime time,
                                BigDecimal price, int seats) {
        try {
            Showtime showtime = new Showtime(movieId, date, time, price, seats);
            return showtimeRepository.create(showtime);
        } catch (SQLException e) {
            System.out.println("Error creating showtime: " + e.getMessage());
            return null;
        }
    }

    public List<Showtime> getShowtimesByMovie(int movieId) {
        try {
            return showtimeRepository.findByMovieId(movieId);
        } catch (SQLException e) {
            System.out.println("Error loading showtimes: " + e.getMessage());
            return null;
        }
    }

    public List<Showtime> getAllShowtimes() {
        try {
            return showtimeRepository.findAll();
        } catch (SQLException e) {
            System.out.println("Error loading showtimes: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteShowtime(int id) {
        try {
            showtimeRepository.delete(id);
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting showtime: " + e.getMessage());
            return false;
        }
    }
}
