package de.szut.pms.common.exceptions;

public class EmployeeAlreadyExistsInProjectException extends RuntimeException {
    public EmployeeAlreadyExistsInProjectException(Long id) {
        super("Die Person mit der ID: " + id + " arbeitet bereits am Projekt.");
    }
}
