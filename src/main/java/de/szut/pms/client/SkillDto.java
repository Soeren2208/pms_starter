package de.szut.pms.client;

/**
 * Represents a skill or qualification assigned to an employee.
 *
 * @param skill name of the skill or qualification
 * @param id unique identifier of the skill or qualification
 */
public record SkillDto(
        String skill,
        Long id
) {
}
