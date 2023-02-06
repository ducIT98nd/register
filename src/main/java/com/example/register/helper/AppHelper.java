package com.example.register.helper;

import java.util.Optional;
import java.util.stream.Stream;

public class AppHelper {
    public static Throwable getrootcause(Exception e) {
        Optional<Throwable> rootCause = Stream.iterate(e, Throwable::getCause)
                .filter(element -> element.getCause() == null)
                .findFirst();
        return rootCause.orElse(null);
    }
}
