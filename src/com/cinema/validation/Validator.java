package com.cinema.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Validator<T> {
    private static class Rule<T> {
        final Predicate<T> p; final String msg;
        Rule(Predicate<T> p, String msg) { this.p = p; this.msg = msg; }
    }
    private final List<Rule<T>> rules = new ArrayList<>();
    public Validator<T> rule(Predicate<T> p, String msg) { rules.add(new Rule<>(p, msg)); return this; }

    public void validate(T v) {
        rules.stream().filter(r -> !r.p.test(v)).findFirst()
                .ifPresent(r -> { throw new ValidationException(r.msg); });
    }
}
