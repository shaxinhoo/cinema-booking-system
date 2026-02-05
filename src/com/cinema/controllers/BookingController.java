package com.cinema.controllers;

import com.cinema.security.AuthorizationService;
import com.cinema.security.Role;
import com.cinema.security.SecurityContext;
import com.cinema.services.BookingService;

public class BookingController {
    private final BookingService service;
    private final AuthorizationService authz;

    public BookingController(BookingService service, AuthorizationService authz) {
        this.service = service;
        this.authz = authz;
    }

    public int createBooking(int showtimeId) throws Exception {
        authz.require(Role.ADMIN, Role.MANAGER, Role.EDITOR, Role.CUSTOMER);
        int userId = SecurityContext.getCurrentUser().getId();
        int bookingId = service.createBooking(userId, showtimeId);
        System.out.println("Booking created: " + bookingId);
        return bookingId;
    }

    public void addTicket(int bookingId, int showtimeId, int row, int seat) throws Exception {
        authz.require(Role.ADMIN, Role.MANAGER, Role.EDITOR, Role.CUSTOMER);
        service.addTicket(bookingId, showtimeId, row, seat);
        System.out.println("Ticket added.");
    }

    // ✅ JOIN endpoint (главное требование)
    public void getFullBookingDescription(int bookingId) throws Exception {
        authz.require(Role.ADMIN, Role.MANAGER, Role.EDITOR, Role.CUSTOMER);
        var dto = service.getFullBookingDescription(bookingId);
        if (dto == null) System.out.println("Booking not found");
        else System.out.println(dto);
    }
}
