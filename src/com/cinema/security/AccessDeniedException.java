package com.cinema.security;
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String m) { super(m); }
}
