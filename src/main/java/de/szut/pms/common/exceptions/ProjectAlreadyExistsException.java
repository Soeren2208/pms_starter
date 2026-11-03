package de.szut.pms.common.exceptions;

public class ProjectAlreadyExistsException extends RuntimeException {
    public ProjectAlreadyExistsException(String name, Long id) {
        super("Das Projekt ' " + name + "' für den Kunden mit der id: " + id + " existiert bereits.");
    }
}
