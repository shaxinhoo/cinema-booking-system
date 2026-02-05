package com.cinema.views;

import com.cinema.dto.FullBookingDescription;
import com.cinema.repositories.MovieRepository;
import com.cinema.repositories.ShowtimeRepository;
import com.cinema.security.AuthorizationService;
import com.cinema.security.Role;
import com.cinema.security.SecurityContext;
import com.cinema.services.AuthService;
import com.cinema.services.BookingService;

import java.util.*;
import java.util.stream.Collectors;

public class ConsoleView {
    private final Scanner sc = new Scanner(System.in);

    private final MovieRepository movies = new MovieRepository();
    private final ShowtimeRepository showtimes = new ShowtimeRepository();

    private final AuthService auth = new AuthService();
    private final BookingService bookingService = new BookingService();
    private final AuthorizationService authz = new AuthorizationService();

    private static class MenuItem {
        final String title;
        final Set<Role> roles;
        final Runnable action;
        MenuItem(String title, Set<Role> roles, Runnable action) {
            this.title = title; this.roles = roles; this.action = action;
        }
    }

    public void start() {
        while (true) {
            if (SecurityContext.getCurrentUser() == null) authMenu();
            else mainMenu();
        }
    }

    private void authMenu() {
        System.out.println("\n=== AUTH ===");
        System.out.println("1) Login");
        System.out.println("2) Register (CUSTOMER)");
        System.out.println("0) Exit");
        int ch = readInt("Choose: ");

        try {
            if (ch == 1) {
                var u = auth.login(read("Email: "), read("Password: "));
                if (u == null) System.out.println("Wrong credentials");
                else { SecurityContext.setCurrentUser(u); System.out.println("Logged in: " + u.fullName() + " (" + u.getRole() + ")"); }
            } else if (ch == 2) {
                auth.registerCustomer(read("Email: "), read("Password: "), read("First name: "), read("Last name: "));
                System.out.println("Registered. Now login.");
            } else if (ch == 0) System.exit(0);
        } catch (Exception e) {
            System.out.println("Auth error: " + e.getMessage());
        }
    }

    private void mainMenu() {
        Role role = Role.valueOf(SecurityContext.getCurrentUser().getRole().toUpperCase());
        System.out.println("\nUser: " + SecurityContext.getCurrentUser().fullName() + " | Role: " + role);

        List<MenuItem> items = List.of(
                new MenuItem("Browse showtimes", EnumSet.allOf(Role.class), this::browseShowtimes),
                new MenuItem("Browse movies", EnumSet.allOf(Role.class), this::browseMovies),

                new MenuItem("Buy tickets (create booking + add tickets)", EnumSet.allOf(Role.class), this::buyFlow),
                new MenuItem("GetFullBookingDescription(bookingId) [JOIN demo]", EnumSet.allOf(Role.class), this::joinDemo),

                new MenuItem("Admin-only: secured ping", EnumSet.of(Role.ADMIN), () -> {
                    authz.require(Role.ADMIN);
                    System.out.println("ADMIN secured endpoint OK");
                }),

                new MenuItem("Logout", EnumSet.allOf(Role.class), () -> SecurityContext.clear())
        );

        List<MenuItem> allowed = items.stream().filter(i -> i.roles.contains(role)).collect(Collectors.toList());
        for (int i = 0; i < allowed.size(); i++) System.out.println((i + 1) + ") " + allowed.get(i).title);

        int ch = readInt("Choose: ");
        if (ch < 1 || ch > allowed.size()) return;

        try { allowed.get(ch - 1).action.run(); }
        catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    private void browseShowtimes() {
        try { showtimes.findAllDetailed().forEach(System.out::println); }
        catch (Exception e) { System.out.println(e.getMessage()); }
    }

    private void browseMovies() {
        try { movies.findAll().forEach(System.out::println); }
        catch (Exception e) { System.out.println(e.getMessage()); }
    }

    private void buyFlow() {
        try {
            int showtimeId = readInt("Showtime ID: ");
            int hallId = readInt("Hall ID (from v_showtimes_detail row hall_name mapping in DB): ");
            int bookingId = bookingService.createBooking(SecurityContext.getCurrentUser().getId(), showtimeId);
            System.out.println("Booking created. bookingId=" + bookingId);

            int count = readInt("How many tickets: ");
            for (int i = 0; i < count; i++) {
                System.out.println("Ticket #" + (i + 1));
                int row = readInt("Row: ");
                int seat = readInt("Seat: ");
                bookingService.addTicket(bookingId, showtimeId, hallId, row, seat);
                System.out.println("Ticket added.");
            }

            System.out.println("Done. Use JOIN demo to print full booking.");
        } catch (Exception e) {
            System.out.println("Buy flow error: " + e.getMessage());
        }
    }

    private void joinDemo() {
        try {
            int bookingId = readInt("Booking ID: ");
            FullBookingDescription d = bookingService.getFullBookingDescription(bookingId);
            if (d == null) System.out.println("Booking not found");
            else System.out.println(d);
        } catch (Exception e) {
            System.out.println("JOIN error: " + e.getMessage());
        }
    }

    private String read(String p) { System.out.print(p); return sc.nextLine().trim(); }
    private int readInt(String p) {
        while (true) {
            try { System.out.print(p); return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.println("Invalid number"); }
        }
    }
}
