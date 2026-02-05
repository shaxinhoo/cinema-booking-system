package com.cinema.views;

import com.cinema.controllers.AuthController;
import com.cinema.controllers.BookingController;
import com.cinema.controllers.CategoryController;
import com.cinema.controllers.MovieController;
import com.cinema.controllers.ShowtimeController;
import com.cinema.dto.FullBookingDescription;
import com.cinema.factory.AppFactory;
import com.cinema.models.Movie;
import com.cinema.models.Showtime;
import com.cinema.security.Role;
import com.cinema.security.SecurityContext;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ConsoleView {

    private final Scanner scanner = new Scanner(System.in);

    private final AuthController auth;
    private final MovieController movies;
    private final ShowtimeController showtimes;
    private final CategoryController categories;
    private final BookingController booking;

    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

    public ConsoleView() {
        AppFactory f = new AppFactory();
        this.auth = f.authController();
        this.movies = f.movieController();
        this.showtimes = f.showtimeController();
        this.categories = f.categoryController();
        this.booking = f.bookingController();
    }

    public void start() {
        splash();

        while (true) {
            if (SecurityContext.getCurrentUser() == null) authMenu();
            else mainMenu();
        }
    }

    private void splash() {
        System.out.println("========================================");
        System.out.println("        CINEMA BOOKING SYSTEM");
        System.out.println("     (Chaplin / Kinopark style)");
        System.out.println("========================================");
        System.out.println("Tip: admin login from seed:");
        System.out.println("     admin@cinema.kz / admin123");
        System.out.println();
    }

    // =========================
    // AUTH
    // =========================
    private void authMenu() {
        System.out.println("\n=== Вход / Регистрация ===");
        System.out.println("1) Войти");
        System.out.println("2) Зарегистрироваться (CUSTOMER)");
        System.out.println("0) Выход");

        int ch = readInt("Выберите: ");
        try {
            if (ch == 1) {
                auth.login(read("Email: "), readHidden("Password: "));
            } else if (ch == 2) {
                auth.register(
                        read("Email: "),
                        readHidden("Password (min 6): "),
                        read("Имя: "),
                        read("Фамилия: ")
                );
            } else if (ch == 0) {
                System.out.println("Пока!");
                System.exit(0);
            } else {
                System.out.println("Неверный пункт меню.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // =========================
    // MAIN MENU
    // =========================
    private void mainMenu() {
        Role role = Role.valueOf(SecurityContext.getCurrentUser().getRole().toUpperCase());

        System.out.println("\n========================================");
        System.out.println("Пользователь: " + SecurityContext.getCurrentUser().fullName() + " | Роль: " + role);
        System.out.println("========================================");
        System.out.println("1) Афиша / Сеансы");
        System.out.println("2) Фильмы");
        System.out.println("3) Категории (просмотр)");
        System.out.println("4) Купить билет (быстрая покупка)");
        System.out.println("5) Мои данные брони (JOIN-чек по bookingId)");
        if (role == Role.ADMIN || role == Role.MANAGER || role == Role.EDITOR) {
            System.out.println("6) [ADMIN/MANAGER/EDITOR] Добавить категорию");
        }
        if (role == Role.ADMIN) {
            System.out.println("7) [ADMIN] Удалить категорию");
        }
        System.out.println("9) Выйти из аккаунта");

        int ch = readInt("Выберите: ");
        try {
            if (ch == 1) showShowtimesBeautiful();
            else if (ch == 2) showMoviesBeautiful();
            else if (ch == 3) categories.listCategories(); // можно оставить печать из контроллера
            else if (ch == 4) buyTicketsWizard();
            else if (ch == 5) joinReceipt();
            else if (ch == 6 && (role == Role.ADMIN || role == Role.MANAGER || role == Role.EDITOR)) {
                categories.createCategory(read("Название категории: "));
            }
            else if (ch == 7 && role == Role.ADMIN) {
                categories.deleteCategory(readInt("ID категории: "));
            }
            else if (ch == 9) auth.logout();
            else System.out.println("Неверный пункт меню.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // =========================
    // Showtimes (pretty)
    // =========================
    private void showShowtimesBeautiful() throws Exception {
        List<Showtime> list = showtimes.getShowtimes(); // ✅ метод должен возвращать List<Showtime>

        if (list.isEmpty()) {
            System.out.println("Сеансы не найдены.");
            return;
        }

        // Группируем по дате
        Map<String, List<Showtime>> byDate = list.stream()
                .collect(Collectors.groupingBy(s -> s.getShowDate().format(dateFmt), TreeMap::new, Collectors.toList()));

        System.out.println("\n=== СЕАНСЫ ===");
        for (String d : byDate.keySet()) {
            System.out.println("\nДата: " + d);
            printShowtimeTable(byDate.get(d));
        }

        System.out.println("\nПодсказка: Чтобы купить, выберите 'Купить билет' и введите Showtime ID.");
    }

    private void printShowtimeTable(List<Showtime> showtimes) {
        // Шапка
        System.out.printf("%-6s %-6s %-22s %-20s %-16s %-8s %-8s%n",
                "ID", "Время", "Фильм", "Кинотеатр", "Зал", "Формат", "Цена");

        System.out.println("-----------------------------------------------------------------------------------------------");

        for (Showtime s : showtimes) {
            System.out.printf("%-6d %-6s %-22s %-20s %-16s %-8s %-8s%n",
                    s.getId(),
                    s.getShowTime().format(timeFmt),
                    cut(s.getMovieTitle(), 22),
                    cut(s.getCinemaName(), 20),
                    cut(s.getHallName(), 16),
                    cut(s.getFormat(), 8),
                    money(s.getBasePrice())
            );
        }
    }

    // =========================
    // Movies (pretty)
    // =========================
    private void showMoviesBeautiful() throws Exception {
        List<Movie> list = movies.getMovies(); // ✅ метод должен возвращать List<Movie>

        if (list.isEmpty()) {
            System.out.println("Фильмы не найдены.");
            return;
        }

        System.out.println("\n=== ФИЛЬМЫ ===");
        System.out.printf("%-5s %-26s %-14s %-10s %-10s %-12s%n", "ID", "Название", "Категория", "Жанр", "Рейтинг", "Длительность");
        System.out.println("---------------------------------------------------------------------------------");

        for (Movie m : list) {
            String cat = (m.getCategoryName() != null) ? m.getCategoryName() : (m.getCategoryId() == null ? "-" : String.valueOf(m.getCategoryId()));
            System.out.printf("%-5d %-26s %-14s %-10s %-10s %-12s%n",
                    m.getId(),
                    cut(m.getTitle(), 26),
                    cut(cat, 14),
                    cut(m.getGenre(), 10),
                    cut(m.getRating(), 10),
                    m.getDurationMinutes() == null ? "-" : (m.getDurationMinutes() + " мин")
            );
        }
    }

    // =========================
    // Purchase wizard
    // =========================
    private void buyTicketsWizard() throws Exception {
        System.out.println("\n=== ПОКУПКА БИЛЕТОВ ===");
        System.out.println("Шаг 1) Выберите Showtime ID из 'Афиша / Сеансы'");
        int showtimeId = readInt("Showtime ID: ");

        int bookingId = booking.createBooking(showtimeId);

        System.out.println("\n✅ Бронь создана. bookingId = " + bookingId);
        System.out.println("Шаг 2) Выберите места (ряд/место).");
        System.out.println("Пример: ряд 5 место 10");
        System.out.println();

        int count = readInt("Сколько билетов купить? ");
        for (int i = 1; i <= count; i++) {
            System.out.println("\nБилет #" + i);
            int row = readInt("Ряд: ");
            int seat = readInt("Место: ");
            booking.addTicket(bookingId, showtimeId, row, seat);
        }

        System.out.println("\n✅ Готово! Хотите распечатать полный чек (JOIN)?");
        System.out.println("1) Да\n0) Нет");
        int ch = readInt("Выберите: ");
        if (ch == 1) booking.getFullBookingDescription(bookingId);
    }

    // =========================
    // JOIN receipt
    // =========================
    private void joinReceipt() throws Exception {
        System.out.println("\n=== ПОЛНЫЙ ЧЕК (JOIN) ===");
        int bookingId = readInt("Введите bookingId: ");
        booking.getFullBookingDescription(bookingId);
    }

    // =========================
    // Helpers
    // =========================
    private String read(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // без настоящего скрытия (консоль IDE часто не поддерживает), но оставляем API
    private String readHidden(String prompt) {
        return read(prompt);
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Введите число.");
            }
        }
    }

    private String cut(String s, int max) {
        if (s == null) return "-";
        if (s.length() <= max) return s;
        return s.substring(0, Math.max(0, max - 1)) + "…";
    }

    private String money(BigDecimal v) {
        if (v == null) return "-";
        // KZT style without decimals in UI
        return v.setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString() + " ₸";
    }
}
