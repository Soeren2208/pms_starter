package de.szut.pms.project;

import de.szut.pms.client.*;
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
        if (!dummyCustomerClient.existsById(dto.customerId())) {
            throw new CustomerNotFoundException(dto.customerId());
        }
        // prüft ob Lead ID existiert
        try {
            externalApiClient.getEmployeeById(dto.projectLeadId());
        } catch (Exception e) {
            throw new EmployeeNotFoundException(dto.projectLeadId());
        }


        // prüft ob IDs (falls angegeben) gültig sind
        List<QualificationDto> qualifications =
                externalApiClient.getAllQualifications();

        if (dto.employees() != null) {
            for (CreateProjectEmployeeDto projectEmployeeDto : dto.employees()) {

                Long employeeId = projectEmployeeDto.employeeId();
                Long qualificationId = projectEmployeeDto.qualificationId();

                try {
                    externalApiClient.getEmployeeById(employeeId);
                } catch (Exception e) {
                    throw new EmployeeNotFoundException(employeeId);
                }

                boolean qualificationExists = qualifications.stream()
                        .anyMatch(q -> q.id().equals(qualificationId));

                if (!qualificationExists) {
                    throw new QualificationNotFoundException(
                            qualificationId);
                }

                // Prüfen ob MA im Zeitraum schon busy ist
                List<ProjectEmployee> existingAssignments =
                        projectEmployeeRepository.findByEmployeeId(employeeId);

                for (ProjectEmployee existing : existingAssignments) {
                    // Da die Zeiträume im Projekt liegen, holen wir das verknüpfte Projekt der alten Zuordnung
                    Project existingProject = existing.getProject();

                    // Überschneidungs-Logik zwischen dem neuen Projekt (aus dem dto) und dem bestehenden Projekt
                    // (Start_Neu <= Ende_Alt) UND (Ende_Neu >= Start_Alt)
                    boolean overlaps = !dto.startDate().isAfter(existingProject.getEndDate()) &&
                            !dto.endDate().isBefore(existingProject.getStartDate());

                    if (overlaps) {
                        throw new ProjectPeriodConflictException(
                                employeeId,
                                dto.startDate(),
                                dto.endDate()
                        );
                    }
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

        // prüft ob valide Qualifikations ID
        List<QualificationDto> qualifications =
                externalApiClient.getAllQualifications();

        boolean qualificationExists = qualifications.stream()
                .anyMatch(q -> q.id().equals(dto.qualificationId()));

        if (!qualificationExists) {
            throw new QualificationNotFoundException(
                    dto.qualificationId());
        }
        // prüfen: ist MA schon am Projekt beteiligt
        if (projectEmployeeRepository.existsByProjectIdAndEmployeeId(
                projectId,
                dto.employeeId())) {

            throw new EmployeeAlreadyExistsInProjectException(
                    dto.employeeId());
        }

        // Zeitraum-Prüfung
        List<ProjectEmployee> existingAssignments =
                projectEmployeeRepository.findByEmployeeId(dto.employeeId());

        for (ProjectEmployee existing : existingAssignments) {
            Project existingProject = existing.getProject();

            // Vergleicht die Laufzeit des aktuellen Projekts mit den bestehenden Projekten
            boolean overlaps = !project.getStartDate().isAfter(existingProject.getEndDate()) &&
                    !project.getEndDate().isBefore(existingProject.getStartDate());

            if (overlaps) {
                throw new ProjectPeriodConflictException(
                        dto.employeeId(),
                        project.getStartDate(),
                        project.getEndDate()
                );
            }
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

    public EmployeeProjectsDto getProjectsByEmployeeId(Long employeeId) {
        List<ProjectEmployee> assignments = projectEmployeeRepository.findByEmployeeId(employeeId);
        List<QualificationDto> allQualifications = externalApiClient.getAllQualifications();
        List<EmployeeProjectsDetailsDto> projectDetailsList = new ArrayList<>();

        for (ProjectEmployee assignment : assignments) {
            Project project = assignment.getProject();
            Long qualId = assignment.getQualificationID();

            String qualName = allQualifications.stream()
                    .filter(q -> q.id().equals(qualId))
                    .map(QualificationDto::skill)
                    .findFirst()
                    .orElse("Unbekannte Qualifikation");

            projectDetailsList.add(new EmployeeProjectsDetailsDto(
                    project.getId(),
                    project.getProjectName(),
                    project.getStartDate(),
                    project.getEndDate(),
                    qualId,
                    qualName
            ));
        }

        return new EmployeeProjectsDto(employeeId, projectDetailsList);
    }


}
