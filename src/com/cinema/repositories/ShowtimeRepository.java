package com.cinema.repositories;

import com.cinema.database.DatabaseConnection;
import com.cinema.models.Showtime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeRepository {

    public Showtime create(Showtime showtime) throws SQLException {
        String sql = "INSERT INTO showtimes (movie_id, hall_id, cinema_id, show_date, " +
                "show_time, base_price, format, available_seats, status) " +
                "VALUES (?, 1, 1, ?, ?, ?, '2D', ?, 'active') RETURNING id";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, showtime.getMovieId());
        stmt.setDate(2, Date.valueOf(showtime.getShowDate()));
        stmt.setTime(3, Time.valueOf(showtime.getShowTime()));
        stmt.setBigDecimal(4, showtime.getPrice());
        stmt.setInt(5, showtime.getAvailableSeats());

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            showtime.setId(rs.getInt("id"));
        }

        rs.close();
        stmt.close();
        conn.close();

        return showtime;
    }

    public Showtime findById(int id) throws SQLException {
        String sql = "SELECT s.*, m.title as movie_title FROM showtimes s " +
                "INNER JOIN movies m ON s.movie_id = m.id WHERE s.id = ?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        Showtime showtime = null;

        if (rs.next()) {
            showtime = extractShowtime(rs);
        }

        rs.close();
        stmt.close();
        conn.close();

        return showtime;
    }

    public List<Showtime> findByMovieId(int movieId) throws SQLException {
        String sql = "SELECT s.*, m.title as movie_title FROM showtimes s " +
                "INNER JOIN movies m ON s.movie_id = m.id " +
                "WHERE s.movie_id = ? AND s.show_date >= CURRENT_DATE " +
                "ORDER BY s.show_date, s.show_time";

        List<Showtime> showtimes = new ArrayList<>();

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, movieId);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            showtimes.add(extractShowtime(rs));
        }

        rs.close();
        stmt.close();
        conn.close();

        return showtimes;
    }

    public List<Showtime> findAll() throws SQLException {
        String sql = "SELECT s.*, m.title as movie_title FROM showtimes s " +
                "INNER JOIN movies m ON s.movie_id = m.id " +
                "WHERE s.show_date >= CURRENT_DATE " +
                "ORDER BY s.show_date, s.show_time LIMIT 20";

        List<Showtime> showtimes = new ArrayList<>();

        Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            showtimes.add(extractShowtime(rs));
        }

        rs.close();
        stmt.close();
        conn.close();

        return showtimes;
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM showtimes WHERE id = ?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    private Showtime extractShowtime(ResultSet rs) throws SQLException {
        Showtime showtime = new Showtime();
        showtime.setId(rs.getInt("id"));
        showtime.setMovieId(rs.getInt("movie_id"));
        showtime.setShowDate(rs.getDate("show_date").toLocalDate());
        showtime.setShowTime(rs.getTime("show_time").toLocalTime());
        showtime.setPrice(rs.getBigDecimal("base_price"));
        showtime.setAvailableSeats(rs.getInt("available_seats"));
        showtime.setMovieTitle(rs.getString("movie_title"));

        return showtime;
    }
}
