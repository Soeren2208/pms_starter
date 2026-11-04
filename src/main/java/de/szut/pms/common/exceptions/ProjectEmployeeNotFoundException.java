package de.szut.pms.common.exceptions;

public class ProjectEmployeeNotFoundException extends RuntimeException {

    public ProjectEmployeeNotFoundException(Long projectId, Long employeeId) {
        super("Mitarbeiter mit der ID " + employeeId +
                " ist keinem Projekt mit der ID " + projectId + " zugeordnet.");
    }
}