package de.szut.pms.projectemployee;

// Represents the assignment of an employee to a project
public record ProjectEmployeeDto(
        Long id,
        Long projectId,
        Long employeeId,
        Long qualificationId
) {
}
