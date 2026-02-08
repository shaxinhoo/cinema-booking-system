package com.cinema.repositories;

import com.cinema.database.DatabaseConnection;
import com.cinema.models.Showtime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeRepository {

    public List<Showtime> findAllDetailed() throws SQLException {
        String sql = "SELECT * FROM v_showtimes_detail ORDER BY show_date, show_time";
        List<Showtime> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Showtime s = new Showtime();
                s.setId(rs.getInt("id"));
                s.setShowDate(rs.getDate("show_date").toLocalDate());
                s.setShowTime(rs.getTime("show_time").toLocalTime());
                s.setBasePrice(rs.getBigDecimal("base_price"));
                s.setFormat(rs.getString("format"));
                s.setMovieTitle(rs.getString("movie_title"));
                s.setCinemaName(rs.getString("cinema_name"));
                s.setHallName(rs.getString("hall_name"));
                list.add(s);
            }
        }
        return list;
    }

    public Showtime findById(int showtimeId) throws SQLException {
        String sql = "SELECT id, hall_id, cinema_id, movie_id, show_date, show_time, base_price, format " +
                "FROM showtimes WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, showtimeId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            Showtime s = new Showtime();
            s.setId(rs.getInt("id"));

            try {
                s.getClass().getMethod("setHallId", int.class).invoke(s, rs.getInt("hall_id"));
                s.getClass().getMethod("setMovieId", int.class).invoke(s, rs.getInt("movie_id"));
                s.getClass().getMethod("setCinemaId", int.class).invoke(s, rs.getInt("cinema_id"));
            } catch (Exception ignore) {
            }

            s.setShowDate(rs.getDate("show_date").toLocalDate());
            s.setShowTime(rs.getTime("show_time").toLocalTime());
            s.setBasePrice(rs.getBigDecimal("base_price"));
            s.setFormat(rs.getString("format"));
            return s;
        }
    }
}
