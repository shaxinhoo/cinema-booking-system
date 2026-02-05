package com.cinema.repositories;

import com.cinema.database.DatabaseConnection;
import java.sql.*;

public class SeatRepository {
    public Integer findSeatId(int hallId, int row, int seat) throws SQLException {
        String sql = "SELECT id FROM seats WHERE hall_id=? AND row_number=? AND seat_number=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, hallId);
            ps.setInt(2, row);
            ps.setInt(3, seat);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            return rs.getInt("id");
        }
    }
}
