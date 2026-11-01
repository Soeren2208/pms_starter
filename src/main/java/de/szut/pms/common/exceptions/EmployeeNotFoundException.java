package de.szut.pms.common.exceptions;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long employeeId) {
        super("Mitarbeiter mit der ID " + employeeId + " wurde nicht gefunden.");
    }
}