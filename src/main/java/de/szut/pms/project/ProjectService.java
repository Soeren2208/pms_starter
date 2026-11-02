package de.szut.pms.project;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;

    public ProjectService(ProjectRepository repository, ProjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Transactional
    public ProjectEmployeesDTO findAllEmployeesOfProjectOneByID(Long id) {
        Project project =  repository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        return mapper.toProjectEmployeesDTO(project);
    }

}
