package de.szut.pms.projectemployee;

import jakarta.validation.constraints.NotNull;

/**
 * Request payload for assigning an employee to a project.
 *
 * @param employeeId identifier of the employee to be assigned
 * @param qualificationId identifier of the qualification the employee
 *                        will use within the project
 */
public record CreateProjectEmployeeDto(

        @NotNull Long employeeId,
        @NotNull Long qualificationId
) {
}
