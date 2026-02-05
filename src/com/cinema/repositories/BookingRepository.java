package com.cinema.repositories;

import com.cinema.database.DatabaseConnection;
import com.cinema.dto.FullBookingDescription;
import com.cinema.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {
    public Booking create(int userId, int showtimeId, String code) throws SQLException {
        String sql = "INSERT INTO bookings(user_id, showtime_id, booking_code) VALUES (?,?,?) RETURNING *";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, showtimeId);
            ps.setString(3, code);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return map(rs);
        }
    }

    public void recalcTotal(int bookingId) throws SQLException {
        String sql = "UPDATE bookings SET total_price=(SELECT COALESCE(SUM(price),0) FROM tickets WHERE booking_id=?) WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        }
    }

    // JOIN endpoint
    public FullBookingDescription getFullBookingDescription(int bookingId) throws SQLException {
        String headSql =
                "SELECT b.*, " +
                        " u.email, u.first_name, u.last_name, u.role, " +
                        " s.show_date, s.show_time, s.base_price, s.format, " +
                        " m.title AS movie_title, c.name AS cinema_name, h.name AS hall_name " +
                        "FROM bookings b " +
                        "JOIN users u ON u.id=b.user_id " +
                        "JOIN showtimes s ON s.id=b.showtime_id " +
                        "JOIN movies m ON m.id=s.movie_id " +
                        "JOIN cinemas c ON c.id=s.cinema_id " +
                        "JOIN halls h ON h.id=s.hall_id " +
                        "WHERE b.id=?";

        String ticketsSql =
                "SELECT t.*, seat.row_number, seat.seat_number, seat.seat_type " +
                        "FROM tickets t JOIN seats seat ON seat.id=t.seat_id " +
                        "WHERE t.booking_id=? ORDER BY t.id";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement head = c.prepareStatement(headSql);
             PreparedStatement tps = c.prepareStatement(ticketsSql)) {

            head.setInt(1, bookingId);
            ResultSet rs = head.executeQuery();
            if (!rs.next()) return null;

            Booking b = map(rs);

            User u = new User();
            u.setId(b.getUserId());
            u.setEmail(rs.getString("email"));
            u.setFirstName(rs.getString("first_name"));
            u.setLastName(rs.getString("last_name"));
            u.setRole(rs.getString("role"));

            Showtime s = new Showtime();
            s.setId(b.getShowtimeId());
            s.setShowDate(rs.getDate("show_date").toLocalDate());
            s.setShowTime(rs.getTime("show_time").toLocalTime());
            s.setBasePrice(rs.getBigDecimal("base_price"));
            s.setFormat(rs.getString("format"));

            String movieTitle = rs.getString("movie_title");
            String cinemaName = rs.getString("cinema_name");
            String hallName = rs.getString("hall_name");
            rs.close();

            tps.setInt(1, bookingId);
            ResultSet trs = tps.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (trs.next()) {
                Ticket t = new Ticket();
                t.setId(trs.getInt("id"));
                t.setBookingId(trs.getInt("booking_id"));
                t.setSeatId(trs.getInt("seat_id"));
                t.setPrice(trs.getBigDecimal("price"));
                t.setTicketCode(trs.getString("ticket_code"));
                t.setRowNumber(trs.getInt("row_number"));
                t.setSeatNumber(trs.getInt("seat_number"));
                t.setSeatType(trs.getString("seat_type"));
                tickets.add(t);
            }
            trs.close();

            return new FullBookingDescription(b, u, s, movieTitle, cinemaName, hallName, tickets);
        }
    }

    private Booking map(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setId(rs.getInt("id"));
        b.setUserId(rs.getInt("user_id"));
        b.setShowtimeId(rs.getInt("showtime_id"));
        b.setBookingCode(rs.getString("booking_code"));
        b.setTotalPrice(rs.getBigDecimal("total_price"));
        b.setStatus(rs.getString("status"));
        b.setPaymentStatus(rs.getString("payment_status"));
        return b;
    }
}
