package de.szut.pms.projectemployee;

public record ProjectEmployeeDto(
        Long id,
        Long projectId,
        Long employeeId,
        Long qualificationId
) {
}
