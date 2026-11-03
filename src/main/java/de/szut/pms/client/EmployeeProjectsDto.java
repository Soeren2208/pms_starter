package de.szut.pms.client;

import java.util.List;

public record EmployeeProjectsDto(Long employeeId,
                                  List<EmployeeProjectsDetailsDto> projects) {
}
