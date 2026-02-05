package com.cinema.security;

import com.cinema.models.User;

public final class SecurityContext {
    private static volatile User currentUser;
    private SecurityContext() {}
    public static User getCurrentUser() { return currentUser; }
    public static void setCurrentUser(User u) { currentUser = u; }
    public static void clear() { currentUser = null; }
}
