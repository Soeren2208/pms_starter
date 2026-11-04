package de.szut.pms.projectemployee;

/**
 * Represents the assignment of an employee to a project,
 * including the qualification used within that project.
 *
 * @param id identifier of the assignment
 * @param projectId identifier of the assigned project
 * @param employeeId identifier of the assigned employee
 * @param qualificationId identifier of the employee's qualification in the project
 */
public record ProjectEmployeeDto(
        Long id,
        Long projectId,
        Long employeeId,
        Long qualificationId
) {
}
