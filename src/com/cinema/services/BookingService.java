package com.cinema.services;

import com.cinema.dto.FullBookingDescription;
import com.cinema.repositories.*;
import com.cinema.validation.Validator;

import java.util.UUID;

public class BookingService {
    private final BookingRepository bookings = new BookingRepository();
    private final ShowtimeRepository showtimes = new ShowtimeRepository();
    private final SeatRepository seats = new SeatRepository();
    private final TicketRepository tickets = new TicketRepository();

    public int createBooking(int userId, int showtimeId) throws Exception {
        new Validator<Integer>().rule(x -> x > 0, "Invalid showtimeId").validate(showtimeId);

        String code = ("B" + UUID.randomUUID().toString().replace("-", "")).substring(0, 10).toUpperCase();
        return bookings.create(userId, showtimeId, code).getId();
    }

    public void addTicket(int bookingId, int showtimeId, int hallId, int row, int seat) throws Exception {
        new Validator<Integer>().rule(x -> x > 0, "Row must be > 0").validate(row);
        new Validator<Integer>().rule(x -> x > 0, "Seat must be > 0").validate(seat);

        Integer seatId = seats.findSeatId(hallId, row, seat);
        if (seatId == null) throw new IllegalArgumentException("Seat not found in hall");

        if (tickets.isSeatTakenForShowtime(showtimeId, seatId)) {
            throw new IllegalStateException("Seat is already taken for this showtime");
        }

        String ticketCode = ("T" + UUID.randomUUID().toString().replace("-", "")).substring(0, 12).toUpperCase();
        var price = showtimes.findAllDetailed().stream()
                .filter(s -> s.getId() == showtimeId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found in view"))
                .getBasePrice();

        tickets.create(bookingId, seatId, ticketCode, price);
        bookings.recalcTotal(bookingId);
    }

    public FullBookingDescription getFullBookingDescription(int bookingId) throws Exception {
        new Validator<Integer>().rule(x -> x > 0, "Invalid bookingId").validate(bookingId);
        return bookings.getFullBookingDescription(bookingId);
    }
}
