package de.szut.pms.project;

import de.szut.pms.client.EmployeeDto;
import de.szut.pms.projectemployee.ProjectEmployeeMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectMapper {
    private final ProjectEmployeeMapper projectEmployeeMapper;

    public ProjectMapper(ProjectEmployeeMapper projectEmployeeMapper) {
        this.projectEmployeeMapper = projectEmployeeMapper;
    }

    public ProjectDto toDto(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getProjectName(),
                project.getStartDate(),
                project.getEndDate(),
                project.getProjectLeadId(),
                project.getCustomerId(),
                project.getDescription(),
                List.of()
        );
    }

    public ProjectDto toDto(
            Project project,
            List<EmployeeDto> employees) {

        return new ProjectDto(
                project.getId(),
                project.getProjectName(),
                project.getStartDate(),
                project.getEndDate(),
                project.getProjectLeadId(),
                project.getCustomerId(),
                project.getDescription(),
                employees
        );
    }

    public Project toEntity(CreateProjectDto dto) {
        Project project = new Project();

        project.setProjectName(dto.projectName());
        project.setStartDate(dto.startDate());
        project.setEndDate(dto.endDate());
        project.setProjectLeadId(dto.projectLeadId());
        project.setCustomerId(dto.customerId());
        project.setDescription(dto.description());
        return project;
    }
}
