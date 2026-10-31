package de.szut.pms.project;

import de.szut.pms.client.ExternalApiClient;
import de.szut.pms.client.EmployeeDto;
import de.szut.pms.common.exceptions.ProjectNotFoundException;
import de.szut.pms.projectemployee.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {
    private final ExternalApiClient externalApiClient;
    private final ProjectRepository projectRepository;
    private final ProjectEmployeeRepository projectEmployeeRepository;
    private final ProjectMapper mapper;
    private final ProjectEmployeeMapper projectEmployeeMapper;


    public ProjectService(ProjectRepository projectRepository, ProjectEmployeeRepository projectemployeerepository, ProjectMapper mapper, ExternalApiClient externalApiClient, ProjectEmployeeMapper projectEmployeeMapper) {
        this.projectRepository = projectRepository;
        this.projectEmployeeRepository = projectemployeerepository;
        this.mapper = mapper;
        this.externalApiClient = externalApiClient;
        this.projectEmployeeMapper = projectEmployeeMapper;
    }

    @Transactional
    public ProjectDto create(CreateProjectDto dto){

        // prüft ob ID existiert
        externalApiClient.getEmployeeById(dto.projectLeadId());

        // prüft ob IDs (falls angegeben) gültig sind
        if (dto.employees() != null) {
            for (CreateProjectEmployeeDto projectEmployeeDto : dto.employees()) {
                Long id = projectEmployeeDto.employeeId();
                externalApiClient.getEmployeeById(id);
            }
        }

        Project saved = projectRepository.save(
                mapper.toEntity(dto)
        );

        // baut projectEmployee Objekt
        if(dto.employees() != null){
            for(CreateProjectEmployeeDto projectEmployeeDto : dto.employees()) {
                ProjectEmployee p = new ProjectEmployee();
                p.setEmployeeId(projectEmployeeDto.employeeId());
                p.setProject(saved);
                p.setQualificationID(projectEmployeeDto.qualificationId());
                projectEmployeeRepository.save(p);
            }
        }

        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> findAll(){
        return projectRepository.findAll().stream()
                .map(project -> mapper.toDto(
                        project
                        ))
                        //projectemployeerepository.countByProjectId(project.getId())))
                .toList();
    }

    public ProjectEmployeeDto addEmployeeToProject(
            Long projectId,
            CreateProjectEmployeeDto dto) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException(projectId));

        ProjectEmployee assignment = new ProjectEmployee();

        assignment.setProject(project);
        assignment.setEmployeeId(dto.employeeId());
        assignment.setQualificationID(dto.qualificationId());

        ProjectEmployee saved =
                projectEmployeeRepository.save(assignment);

        return projectEmployeeMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public ProjectDto findById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        List<ProjectEmployee> assignments =
                projectEmployeeRepository.findByProjectId(id);

        List<EmployeeDto> employees = assignments.stream()
                .map(assignment ->
                        externalApiClient.getEmployeeById(
                                assignment.getEmployeeId()))
                .toList();

        return mapper.toDto(project, employees);
    }
}
