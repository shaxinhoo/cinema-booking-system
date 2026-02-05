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
}
