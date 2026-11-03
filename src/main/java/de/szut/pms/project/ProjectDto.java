package de.szut.pms.project;

import de.szut.pms.client.EmployeeDto;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectDto(
        Long id,
        String projectName,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long projectLead,
        Long customerId,
        String description,
        List<EmployeeDto> employees

) {
}
