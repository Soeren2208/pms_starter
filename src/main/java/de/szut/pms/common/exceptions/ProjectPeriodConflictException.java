package de.szut.pms.common.exceptions;

import java.time.LocalDateTime;

public class ProjectPeriodConflictException extends RuntimeException {
    public ProjectPeriodConflictException(Long id, LocalDateTime startDate, LocalDateTime endDate) {
        super("Mitarbeiter*in mit der ID: " + id + " ist im Zeitraum " + startDate + " - " + endDate + " schon an einem anderen Projekt beteiligt.");
    }
}
