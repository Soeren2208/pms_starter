package de.szut.pms.project;


import de.szut.pms.projectemployee.CreateProjectEmployeeDto;
import de.szut.pms.projectemployee.ProjectEmployee;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

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


