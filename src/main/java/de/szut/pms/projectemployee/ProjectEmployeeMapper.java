package de.szut.pms.projectemployee;

import org.springframework.stereotype.Component;

@Component
public class ProjectEmployeeMapper {
    public ProjectEmployeeDto toDto(ProjectEmployee projectEmployee) {
        return new ProjectEmployeeDto(
                projectEmployee.getId(),
                projectEmployee.getProject().getId(),
                projectEmployee.getEmployeeId(),
                projectEmployee.getQualificationID());

    }

    public ProjectEmployee toEntity(CreateProjectEmployeeDto dto){
        ProjectEmployee projectEmployee = new ProjectEmployee();

        //project muss noch geholt werden um es zu setten
        //projectEmployee.setProject();
        projectEmployee.setEmployeeId(dto.employeeId());
        projectEmployee.setQualificationID(dto.qualificationId());
        return projectEmployee;
    }
}
