package de.szut.pms.common.exceptions;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Long id) {
        super("Es gibt kein Projekt mit der Kennung " + id + ".");
    }
}
