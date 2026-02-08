package com.cinema.factory;

import com.cinema.controllers.*;
import com.cinema.repositories.*;
import com.cinema.security.AuthorizationService;
import com.cinema.services.*;

public class AppFactory {

    // вот здеся реализация Factory паттерна
    public UserRepository userRepository() { return new UserRepository(); }
    public MovieRepository movieRepository() { return new MovieRepository(); }
    public ShowtimeRepository showtimeRepository() { return new ShowtimeRepository(); }
    public CategoryRepository categoryRepository() { return new CategoryRepository(); }
    public BookingRepository bookingRepository() { return new BookingRepository(); }
    public SeatRepository seatRepository() { return new SeatRepository(); }
    public TicketRepository ticketRepository() { return new TicketRepository(); }

    public PasswordHasher passwordHasher() { return new PasswordHasher(); }
    public AuthService authService() { return new AuthService(); }
    public AuthorizationService authorizationService() { return new AuthorizationService(); }
    public CategoryService categoryService() { return new CategoryService(); }
    public BookingService bookingService() { return new BookingService(); }

    public AuthController authController() { return new AuthController(authService()); }
    public MovieController movieController() { return new MovieController(movieRepository(), authorizationService()); }
    public ShowtimeController showtimeController() { return new ShowtimeController(showtimeRepository(), authorizationService()); }
    public CategoryController categoryController() { return new CategoryController(categoryService(), authorizationService()); }
    public BookingController bookingController() { return new BookingController(bookingService(), authorizationService()); }
}
