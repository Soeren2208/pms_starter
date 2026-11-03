package de.szut.pms.projectemployee;

import jakarta.validation.constraints.NotNull;

public record CreateProjectEmployeeDto(

        @NotNull Long employeeId,
        @NotNull Long qualificationId
) {
}
