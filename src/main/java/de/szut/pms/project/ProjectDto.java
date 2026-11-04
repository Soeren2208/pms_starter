package de.szut.pms.project;

import de.szut.pms.client.EmployeeDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a project managed by the Project Management Service.
 *
 * @param id unique identifier of the project
 * @param projectName name of the project
 * @param startDate planned start date of the project
 * @param endDate planned end date of the project
 * @param projectLead identifier of the project lead
 * @param customerId identifier of the customer
 * @param description optional description of the project
 * @param employees employees assigned to the project
 */
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
