/*package de.szut.pms.projectemployee;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectEmployeeService {

    private final ProjectEmployeeRepository projectEmployeeRepository;
    private final ProjectEmployeeMapper mapper;

    public ProjectEmployeeService(ProjectEmployeeRepository projectEmployeeRepository, ProjectEmployeeMapper mapper) {
        this.projectEmployeeRepository = projectEmployeeRepository;
        this.mapper = mapper;
    }

    public ProjectEmployeeDto create(@Valid CreateProjectEmployeeDto dto) {
        return null;
    }

    @Transactional(readOnly = true)
    public ProjectEmployeeDto findById(Long id) {
        ProjectEmployee projectEmployee = projectEmployeeRepository.findById(id)
                .orElseThrow()->new ProjectNotFoundException(id));
        return mapper.toDto(projectEmployee, );
    }
}*/
