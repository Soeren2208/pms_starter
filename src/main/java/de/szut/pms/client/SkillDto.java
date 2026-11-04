package de.szut.pms.client;

import io.swagger.v3.oas.annotations.media.Schema;

// Represents a skill or qualification assigned to an employee.
@Schema(description = "Represents a skill or qualification assigned to an employee.")
public record SkillDto(
        @Schema(description = "name of the skill or qualification", example = "Java Backend Development", requiredMode = Schema.RequiredMode.REQUIRED)
        String skill,

        @Schema(description = "unique identifier of the skill or qualification", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id
) {
}
