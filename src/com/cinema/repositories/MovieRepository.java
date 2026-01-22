package com.cinema.repositories;

import com.cinema.database.DatabaseConnection;
import com.cinema.models.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    public Movie create(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, description, duration_minutes, genre, " +
                "rating, release_date, director, movie_cast, language) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, movie.getTitle());
        stmt.setString(2, movie.getDescription());
        stmt.setInt(3, movie.getDurationMinutes());
        stmt.setString(4, movie.getGenre());
        stmt.setString(5, movie.getRating());
        stmt.setDate(6, Date.valueOf(movie.getReleaseDate()));
        stmt.setString(7, movie.getDirector());
        stmt.setString(8, "");
        stmt.setString(9, "English");

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            movie.setId(rs.getInt("id"));
        }

        rs.close();
        stmt.close();
        conn.close();

        return movie;
    }

    public Movie findById(int id) throws SQLException {
        String sql = "SELECT * FROM movies WHERE id = ?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        Movie movie = null;

        if (rs.next()) {
            movie = extractMovie(rs);
        }

        rs.close();
        stmt.close();
        conn.close();

        return movie;
    }

    public List<Movie> findAll() throws SQLException {
        String sql = "SELECT * FROM movies ORDER BY release_date DESC";
        List<Movie> movies = new ArrayList<>();

        Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            movies.add(extractMovie(rs));
        }

        rs.close();
        stmt.close();
        conn.close();

        return movies;
    }

    public List<Movie> searchByTitle(String keyword) throws SQLException {
        String sql = "SELECT * FROM movies WHERE title ILIKE ? ORDER BY release_date DESC";
        List<Movie> movies = new ArrayList<>();

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "%" + keyword + "%");

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            movies.add(extractMovie(rs));
        }

        rs.close();
        stmt.close();
        conn.close();

        return movies;
    }

    public void update(Movie movie) throws SQLException {
        String sql = "UPDATE movies SET title=?, description=?, duration_minutes=?, " +
                "genre=?, rating=?, release_date=?, director=? WHERE id=?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, movie.getTitle());
        stmt.setString(2, movie.getDescription());
        stmt.setInt(3, movie.getDurationMinutes());
        stmt.setString(4, movie.getGenre());
        stmt.setString(5, movie.getRating());
        stmt.setDate(6, Date.valueOf(movie.getReleaseDate()));
        stmt.setString(7, movie.getDirector());
        stmt.setInt(8, movie.getId());

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM movies WHERE id = ?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    private Movie extractMovie(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setId(rs.getInt("id"));
        movie.setTitle(rs.getString("title"));
        movie.setDescription(rs.getString("description"));
        movie.setDurationMinutes(rs.getInt("duration_minutes"));
        movie.setGenre(rs.getString("genre"));
        movie.setRating(rs.getString("rating"));

        Date releaseDate = rs.getDate("release_date");
        if (releaseDate != null) {
            movie.setReleaseDate(releaseDate.toLocalDate());
        }

        movie.setDirector(rs.getString("director"));

        return movie;
    }
}
