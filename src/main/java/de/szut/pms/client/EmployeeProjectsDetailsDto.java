package de.szut.pms.client;

import java.time.LocalDateTime;

public record EmployeeProjectsDetailsDto(Long projectId,
                                         String projectName,
                                         LocalDateTime startDate,
                                         LocalDateTime endDate,
                                         Long qualificationId,
                                         String qualificationName) {
}
