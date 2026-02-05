package com.cinema.repositories;

import com.cinema.database.DatabaseConnection;
import com.cinema.models.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {
    public List<Movie> findAll() throws SQLException {
        String sql = "SELECT m.*, c.name AS category_name " +
                "FROM movies m LEFT JOIN categories c ON c.id=m.category_id " +
                "ORDER BY m.release_date DESC NULLS LAST, m.id DESC";
        List<Movie> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Movie m = new Movie();
                m.setId(rs.getInt("id"));
                Object cid = rs.getObject("category_id");
                m.setCategoryId(cid == null ? null : rs.getInt("category_id"));
                m.setCategoryName(rs.getString("category_name"));
                m.setTitle(rs.getString("title"));
                m.setGenre(rs.getString("genre"));
                m.setRating(rs.getString("rating"));
                Object dur = rs.getObject("duration_minutes");
                m.setDurationMinutes(dur == null ? null : rs.getInt("duration_minutes"));
                Date rd = rs.getDate("release_date");
                if (rd != null) m.setReleaseDate(rd.toLocalDate());
                list.add(m);
            }
        }
        return list;
    }
}
