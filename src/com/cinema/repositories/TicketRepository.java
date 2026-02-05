package com.cinema.repositories;

import com.cinema.database.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;

public class TicketRepository {
    public boolean isSeatTakenForShowtime(int showtimeId, int seatId) throws SQLException {
        String sql =
                "SELECT 1 FROM tickets t " +
                        "JOIN bookings b ON b.id=t.booking_id " +
                        "WHERE b.showtime_id=? AND t.seat_id=? AND b.status IN ('PENDING','CONFIRMED') " +
                        "LIMIT 1";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, showtimeId);
            ps.setInt(2, seatId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void create(int bookingId, int seatId, String code, BigDecimal price) throws SQLException {
        String sql = "INSERT INTO tickets(booking_id, seat_id, price, ticket_code) VALUES (?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.setInt(2, seatId);
            ps.setBigDecimal(3, price);
            ps.setString(4, code);
            ps.executeUpdate();
        }
    }
}
