package com.cinema.views;

import com.cinema.controllers.MovieController;
import com.cinema.controllers.ShowtimeController;
import com.cinema.models.Movie;
import com.cinema.models.Showtime;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class ConsoleView {
    private Scanner scanner;
    private MovieController movieController;
    private ShowtimeController showtimeController;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
        this.movieController = new MovieController();
        this.showtimeController = new ShowtimeController();
    }

    public void start() {
        while (true) {
            printMainMenu();
            int choice = getIntInput("Choose option: ");

            switch (choice) {
                case 1:
                    movieMenu();
                    break;
                case 2:
                    showtimeMenu();
                    break;
                case 0:
                    System.out.println("exited");
                    return;
                default:
                    System.out.println("invalid");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=== CINEMA BOOKING SYSTEM ===");
        System.out.println("1. Movie Management");
        System.out.println("2. Showtime Management");
        System.out.println("0. Exit");
        System.out.println("=============================");
    }

    private void movieMenu() {
        while (true) {
            System.out.println("\n=== MOVIE MANAGEMENT ===");
            System.out.println("1. View all movies");
            System.out.println("2. Search movie");
            System.out.println("3. Add new movie");
            System.out.println("4. Update movie");
            System.out.println("5. Delete movie");
            System.out.println("0. Back");

            int choice = getIntInput("Choose option: ");

            switch (choice) {
                case 1:
                    viewAllMovies();
                    break;
                case 2:
                    searchMovie();
                    break;
                case 3:
                    addMovie();
                    break;
                case 4:
                    updateMovie();
                    break;
                case 5:
                    deleteMovie();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("invalid");
            }
        }
    }

    private void showtimeMenu() {
        while (true) {
            System.out.println("\n=== SHOWTIME MANAGEMENT ===");
            System.out.println("1. View all showtimes");
            System.out.println("2. View showtimes for movie");
            System.out.println("3. Add new showtime");
            System.out.println("4. Delete showtime");
            System.out.println("0. Back");

            int choice = getIntInput("Choose option: ");

            switch (choice) {
                case 1:
                    viewAllShowtimes();
                    break;
                case 2:
                    viewShowtimesForMovie();
                    break;
                case 3:
                    addShowtime();
                    break;
                case 4:
                    deleteShowtime();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("invalid");
            }
        }
    }

    private void viewAllMovies() {
        List<Movie> movies = movieController.getAllMovies();

        if (movies == null || movies.isEmpty()) {
            System.out.println("No movies found");
            return;
        }

        System.out.println("\n=== ALL MOVIES ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }

    private void searchMovie() {
        String keyword = getStringInput("Enter movie title: ");
        List<Movie> movies = movieController.searchMovies(keyword);

        if (movies == null || movies.isEmpty()) {
            System.out.println("No movies found");
            return;
        }

        System.out.println("\n=== SEARCH RESULTS ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }

    private void addMovie() {
        System.out.println("\n=== ADD NEW MOVIE ===");
        String title = getStringInput("Title: ");
        String description = getStringInput("Description: ");
        int duration = getIntInput("Duration (минуты): ");
        String genre = getStringInput("Genre: ");
        String rating = getStringInput("Rating (G/PG/PG-13/R): ");
        String dateStr = getStringInput("Release date (год-месяц-день): ");
        LocalDate releaseDate = LocalDate.parse(dateStr);
        String director = getStringInput("Director: ");

        Movie movie = movieController.addMovie(title, description, duration,
                genre, rating, releaseDate, director);

        if (movie != null) {
            System.out.println("Movie added with id: " + movie.getId());
        }
    }

    private void updateMovie() {
        int id = getIntInput("Enter movie id: ");
        Movie existing = movieController.getMovieById(id);

        if (existing == null) {
            System.out.println("Movie not found");
            return;
        }

        System.out.println("Current: " + existing);
        System.out.println("\n=== UPDATE MOVIE ===");

        String title = getStringInput("Title [" + existing.getTitle() + "]: ");
        if (title.isEmpty()) title = existing.getTitle();

        String description = getStringInput("Description: ");
        if (description.isEmpty()) description = existing.getDescription();

        int duration = getIntInput("Duration [" + existing.getDurationMinutes() + "]: ");
        if (duration == 0) duration = existing.getDurationMinutes();

        String genre = getStringInput("Genre [" + existing.getGenre() + "]: ");
        if (genre.isEmpty()) genre = existing.getGenre();

        String rating = getStringInput("Rating [" + existing.getRating() + "]: ");
        if (rating.isEmpty()) rating = existing.getRating();

        String director = getStringInput("Director [" + existing.getDirector() + "]: ");
        if (director.isEmpty()) director = existing.getDirector();

        boolean success = movieController.updateMovie(id, title, description, duration,
                genre, rating, existing.getReleaseDate(), director);

        if (success) {
            System.out.println("Movie updated");
        }
    }

    private void deleteMovie() {
        int id = getIntInput("Enter movie id to delete: ");
        Movie movie = movieController.getMovieById(id);

        if (movie == null) {
            System.out.println("Movie not found");
            return;
        }

        System.out.println("Delete: " + movie);
        String confirm = getStringInput("Confirm (yes/no): ");

        if (confirm.equalsIgnoreCase("yes")) {
            if (movieController.deleteMovie(id)) {
                System.out.println("Movie deleted");
            }
        }
    }

    private void viewAllShowtimes() {
        List<Showtime> showtimes = showtimeController.getAllShowtimes();

        if (showtimes == null || showtimes.isEmpty()) {
            System.out.println("No showtimes found");
            return;
        }

        System.out.println("\n=== ALL SHOWTIMES ===");
        for (Showtime showtime : showtimes) {
            System.out.println(showtime);
        }
    }

    private void viewShowtimesForMovie() {
        int movieId = getIntInput("Enter movie id: ");
        List<Showtime> showtimes = showtimeController.getShowtimesByMovie(movieId);

        if (showtimes == null || showtimes.isEmpty()) {
            System.out.println("No showtimes found for this movie");
            return;
        }

        System.out.println("\n=== SHOWTIMES ===");
        for (Showtime showtime : showtimes) {
            System.out.println(showtime);
        }
    }

    private void addShowtime() {
        System.out.println("\n=== ADD NEW SHOWTIME ===");
        int movieId = getIntInput("Movie id: ");
        String dateStr = getStringInput("Date (год-месяц-день): ");
        LocalDate date = LocalDate.parse(dateStr);
        String timeStr = getStringInput("Time (часы-минуты): ");
        LocalTime time = LocalTime.parse(timeStr);
        int price = getIntInput("Price (kzt): ");
        int seats = getIntInput("Available seats: ");

        Showtime showtime = showtimeController.addShowtime(movieId, date, time,
                new BigDecimal(price), seats);

        if (showtime != null) {
            System.out.println("Showtime added with id: " + showtime.getId());
        }
    }

    private void deleteShowtime() {
        int id = getIntInput("Enter showtime id to delete: ");

        String confirm = getStringInput("Confirm delete (yes/no): ");

        if (confirm.equalsIgnoreCase("yes")) {
            if (showtimeController.deleteShowtime(id)) {
                System.out.println("Showtime deleted");
            }
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) return 0;
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("invalid number");
            }
        }
    }
}

