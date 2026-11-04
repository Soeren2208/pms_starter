package de.szut.pms.client;

import java.util.List;

/**
 * Represents an employee retrieved from the Employee Service.
 *
 * @param id unique identifier of the employee
 * @param lastName employee's last name
 * @param firstName employee's first name
 * @param street street address of the employee
 * @param postcode postal code of the employee
 * @param city city of residence
 * @param phone employee's phone number
 * @param skillSet list of skills assigned to the employee
 */
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
