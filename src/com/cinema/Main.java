package com.cinema;

import com.cinema.database.DatabaseConnection;
import com.cinema.views.ConsoleView;

public class Main {
    public static void main(String[] args) {
        System.out.println("Cinema Booking System - Starting...\n");

        System.out.println("Testing database connection...");
        DatabaseConnection.testConnection();

        System.out.println("\nPress Enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
        }

        ConsoleView view = new ConsoleView();
        view.start();
    }
}
