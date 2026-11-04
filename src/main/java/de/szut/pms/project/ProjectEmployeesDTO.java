package de.szut.pms.project;

import de.szut.pms.projectemployee.ProjectEmployeeDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProjectEmployeesDTO {
    private Long projectId;
    private String description;
    private List<ProjectEmployeeDTO> employees;

}
