package de.szut.pms.client;

import java.util.List;

// Represents an employee retrieved from the Employee Service.
public record EmployeeDto(
        Long id,
        String lastName,
        String firstName,
        String street,
        String postcode,
        String city,
        String phone,
        List<SkillDto> skillSet
) {
}
