package de.szut.pms.project;

import de.szut.pms.projectemployee.ProjectEmployee;
import de.szut.pms.projectemployee.ProjectEmployeeNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;

    public ProjectService(ProjectRepository repository, ProjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ProjectEmployeeRemoveDTO removeEmployee(Long projectId, Long employeeId){
        Project project = repository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException(projectId));

        ProjectEmployee deleteEmployee = null;
        for(ProjectEmployee employee: project.getEmployee()){
            if(employeeId.equals(employee.getEmployeeId())){
                deleteEmployee = employee;
                break;
            }
        }
        if(deleteEmployee != null){
            project.getEmployee().remove(deleteEmployee);
        }else{
            throw new ProjectEmployeeNotFoundException(projectId, employeeId);
        }
        repository.save(project);
        return mapper.toRemoveEmployeeDTO(project);
    }


}
