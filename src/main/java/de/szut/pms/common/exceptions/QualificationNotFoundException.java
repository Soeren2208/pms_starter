package de.szut.pms.common.exceptions;

public class QualificationNotFoundException extends RuntimeException {
    public QualificationNotFoundException(Long id) {
        super("Es existiert keine Qualifikation mit der ID: " + id);
    }
}
