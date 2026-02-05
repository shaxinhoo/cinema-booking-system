package com.cinema.dto;

import com.cinema.models.Booking;
import com.cinema.models.Showtime;
import com.cinema.models.Ticket;
import com.cinema.models.User;

import java.util.List;

public class FullBookingDescription {
    public final Booking booking;
    public final User user;
    public final Showtime showtime;
    public final String movieTitle;
    public final String cinemaName;
    public final String hallName;
    public final List<Ticket> tickets;

    public FullBookingDescription(Booking b, User u, Showtime s, String mt, String cn, String hn, List<Ticket> t) {
        booking = b; user = u; showtime = s; movieTitle = mt; cinemaName = cn; hallName = hn; tickets = t;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== FULL BOOKING DESCRIPTION (JOIN) ===\n");
        sb.append("Booking: ").append(booking).append("\n");
        sb.append("User: ").append(user.fullName()).append(" (").append(user.getRole()).append(") ").append(user.getEmail()).append("\n");
        sb.append("Movie: ").append(movieTitle).append("\n");
        sb.append("Cinema: ").append(cinemaName).append(" | Hall: ").append(hallName).append("\n");
        sb.append("Showtime: ").append(showtime).append("\n");
        sb.append("Tickets:\n");
        for (Ticket t : tickets) sb.append("  - ").append(t).append("\n");
        return sb.toString();
    }
}
