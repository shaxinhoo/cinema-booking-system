package com.cinema.views;

import com.cinema.repositories.MovieRepository;
import com.cinema.repositories.ShowtimeRepository;
import com.cinema.security.SecurityContext;
import com.cinema.services.AuthService;

import java.util.Scanner;

public class ConsoleView {
    private final Scanner sc = new Scanner(System.in);

    private final MovieRepository movies = new MovieRepository();
    private final ShowtimeRepository showtimes = new ShowtimeRepository();
    private final AuthService auth = new AuthService();

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
        System.out.println("\n=== MENU ===");
        System.out.println("User: " + SecurityContext.getCurrentUser().fullName() + " | Role: " + SecurityContext.getCurrentUser().getRole());
        System.out.println("1) Browse showtimes");
        System.out.println("2) Browse movies");
        System.out.println("9) Logout");

        int ch = readInt("Choose: ");
        try {
            if (ch == 1) showtimes.findAllDetailed().forEach(System.out::println);
            else if (ch == 2) movies.findAll().forEach(System.out::println);
            else if (ch == 9) SecurityContext.clear();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
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
