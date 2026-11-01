package de.szut.pms.common.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long customerId) {
        super("Kunde mit der ID " + customerId + " wurde nicht gefunden.");
    }
}