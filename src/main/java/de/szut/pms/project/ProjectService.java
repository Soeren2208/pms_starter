package de.szut.pms.project;

import de.szut.pms.client.ExternalApiClient;
import de.szut.pms.client.EmployeeDto;
import de.szut.pms.common.exceptions.*;
import de.szut.pms.customer.DummyCustomerClient;
import de.szut.pms.projectemployee.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private final ExternalApiClient externalApiClient;
    private final DummyCustomerClient dummyCustomerClient;
    private final ProjectRepository projectRepository;
    private final ProjectEmployeeRepository projectEmployeeRepository;
    private final ProjectMapper mapper;
    private final ProjectEmployeeMapper projectEmployeeMapper;


    public ProjectService(ProjectRepository projectRepository, ProjectEmployeeRepository projectemployeerepository, ProjectMapper mapper, ExternalApiClient externalApiClient, DummyCustomerClient dummyCustomerClient, ProjectEmployeeMapper projectEmployeeMapper) {
        this.projectRepository = projectRepository;
        this.projectEmployeeRepository = projectemployeerepository;
        this.mapper = mapper;
        this.externalApiClient = externalApiClient;
        this.dummyCustomerClient = dummyCustomerClient;
        this.projectEmployeeMapper = projectEmployeeMapper;
    }

    @Transactional
    public ProjectDto create(CreateProjectDto dto) {

        // prüfen ob Projekt schon existiert und wenn ja, Exception werfen
        if (projectRepository.existsByProjectNameAndCustomerId(dto.projectName(), dto.customerId())) {
            throw new ProjectAlreadyExistsException(dto.projectName(), dto.customerId());
        }

        // prüft CustomerID
        if(!dummyCustomerClient.existsById(dto.customerId())) {
            throw new CustomerNotFoundException(dto.customerId());
        }
        // prüft ob Lead ID existiert
        try {
            externalApiClient.getEmployeeById(dto.projectLeadId());
        } catch (Exception e) {
            throw new EmployeeNotFoundException(dto.projectLeadId());
        }


        // prüft ob IDs (falls angegeben) gültig sind
        if (dto.employees() != null) {
            for (CreateProjectEmployeeDto projectEmployeeDto : dto.employees()) {
                Long id = projectEmployeeDto.employeeId();

                try {
                    externalApiClient.getEmployeeById(id);
                } catch (Exception e) {
                    throw new EmployeeNotFoundException(id);
                }
            }
        }

        Project saved = projectRepository.save(
                mapper.toEntity(dto)
        );

        // baut projectEmployee Objekt
        List<EmployeeDto> employees = new ArrayList<>();
        if (dto.employees() != null) {
            for (CreateProjectEmployeeDto projectEmployeeDto : dto.employees()) {
                EmployeeDto employee =
                        externalApiClient.getEmployeeById(
                                projectEmployeeDto.employeeId());

                employees.add(employee);

                ProjectEmployee p = new ProjectEmployee();
                p.setEmployeeId(projectEmployeeDto.employeeId());
                p.setProject(saved);
                p.setQualificationID(projectEmployeeDto.qualificationId());

                projectEmployeeRepository.save(p);
            }
            return mapper.toDto(saved, employees);
        }

        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> findAll() {
        return projectRepository.findAll().stream()
                .map(project -> {

                    List<ProjectEmployee> assignments =
                            projectEmployeeRepository.findByProjectId(
                                    project.getId());

                    List<EmployeeDto> employees = assignments.stream()
                            .map(assignment ->
                                    externalApiClient.getEmployeeById(
                                            assignment.getEmployeeId()))
                            .toList();

                    return mapper.toDto(project, employees);
                })
                .toList();
    }

    public ProjectEmployeeDto addEmployeeToProject(
            Long projectId,
            CreateProjectEmployeeDto dto) {

        ProjectEmployee assignment = new ProjectEmployee();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));


        // prüfen ob valide ID des MAs
        try {
            externalApiClient.getEmployeeById(dto.employeeId());
        } catch (Exception e) {
            throw new EmployeeNotFoundException(dto.employeeId());
        }

        // prüfen: ist MA schon am Projekt beteiligt
        if (projectEmployeeRepository.existsByProjectIdAndEmployeeId(
                projectId,
                dto.employeeId())) {

            throw new EmployeeAlreadyExistsInProjectException(
                    dto.employeeId());
        }


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
