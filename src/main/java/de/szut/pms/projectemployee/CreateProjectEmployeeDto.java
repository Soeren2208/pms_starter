package de.szut.pms.projectemployee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

// Request payload for assigning an employee to a project.
@Schema(description = "Request payload for assigning an employee to a project.")
public record CreateProjectEmployeeDto(

        @Schema(description = "identifier of the employee to be assigned", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Long employeeId,

        @Schema(description = "identifier of the qualification the employee will use within the project", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Long qualificationId
) {
}
