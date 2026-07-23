package com.codeartify.fitness.membership.application;

public class DuplicateEmailAddressException extends RuntimeException {
    public DuplicateEmailAddressException(String emailAddress) {
        super("Email address " + emailAddress + " is already registered.");
    }
}
