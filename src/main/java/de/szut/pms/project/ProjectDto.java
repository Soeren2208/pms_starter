package de.szut.pms.project;

import de.szut.pms.client.EmployeeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

// Represents a project managed by the Project Management Service.
@Schema(description = "Represents a project managed by the Project Management Service.")
public record ProjectDto(
        @Schema(description = "unique identifier of the project", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,

        @Schema(description = "name of the project", example = "E-Commerce Migration", requiredMode = Schema.RequiredMode.REQUIRED)
        String projectName,

        @Schema(description = "planned start date of the project", example = "2026-03-01T08:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime startDate,

        @Schema(description = "planned end date of the project", example = "2026-09-30T17:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime endDate,

        @Schema(description = "identifier of the project lead", example = "42", requiredMode = Schema.RequiredMode.REQUIRED)
        Long projectLead,

        @Schema(description = "identifier of the customer", example = "109", requiredMode = Schema.RequiredMode.REQUIRED)
        Long customerId,

        @Schema(description = "optional description of the project", example = "Migration legacy monolith to cloud infrastructure", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String description,

        @Schema(description = "employees assigned to the project", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<EmployeeDto> employees
) {
}
