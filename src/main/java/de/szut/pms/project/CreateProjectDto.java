package de.szut.pms.project;


import de.szut.pms.projectemployee.CreateProjectEmployeeDto;
import de.szut.pms.projectemployee.ProjectEmployee;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Request payload for creating a new project.
 *
 * @param projectName name of the project
 * @param startDate planned start date of the project
 * @param endDate planned end date of the project
 * @param projectLeadId identifier of the project lead
 * @param customerId identifier of the customer
 * @param description optional description of the project
 * @param employees optional list of employees assigned during project creation,
 *                  including their qualifications
 */
public record CreateProjectDto(
        @NotBlank(message = "Der Projektname darf nicht leer sein!")
        String projectName,

        @NotNull(message = "Es muss ein Startdatum geben.")
        LocalDateTime startDate,

        @NotNull(message = "Es muss ein Enddatum geben.")
        LocalDateTime endDate,

        @NotNull(message = "Es muss eine Projektleitung geben.")
        Long projectLeadId,

        @NotNull(message = "Es muss eine Kunden ID angegeben werden.")
        Long customerId,

        String description,

        List<CreateProjectEmployeeDto> employees

) {
}


