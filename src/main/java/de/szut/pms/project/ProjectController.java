package de.szut.pms.project;

import de.szut.pms.client.EmployeeProjectsDto;
import de.szut.pms.projectemployee.CreateProjectEmployeeDto;
import de.szut.pms.projectemployee.ProjectEmployeeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService service;
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectService service, ProjectRepository projectRepository){
        this.service = service;
        this.projectRepository = projectRepository;
    }

    /**
     * Creates a new project.
     *
     * @param dto project data including lead, customer and optional employees
     * @return the created project
     */
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody CreateProjectDto dto){
        ProjectDto created = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    /**
     * Adds an employee to an existing project.
     *
     * @param projectId identifier of the project
     * @param dto employee assignment data including qualification
     * @return the created project employee assignment
     */
    @PostMapping("/{projectId}/employees")
    public ResponseEntity<ProjectEmployeeDto> addEmployeeToProject(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateProjectEmployeeDto dto) {

        ProjectEmployeeDto created =
                service.addEmployeeToProject(projectId, dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    /**
     * Retrieves all projects.
     *
     * @return a list of all available projects
     */
    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Retrieves a project by its ID
     *
     * @param id the identifier of the project
     * @return the requested project
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Retrieves all projects an employee has participated in
     *
     * @param employeeId the identifier of the employee
     * @return a list of projects associated with the employee, including their qualifications
     */
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeProjectsDto> getProjectsByEmployeeId(
            @PathVariable Long employeeId) {

        EmployeeProjectsDto response = service.getProjectsByEmployeeId(employeeId);
        return ResponseEntity.ok(response);
    }

}
