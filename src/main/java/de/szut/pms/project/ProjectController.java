package de.szut.pms.project;

import de.szut.pms.client.EmployeeProjectsDto;
import de.szut.pms.projectemployee.CreateProjectEmployeeDto;
import de.szut.pms.projectemployee.ProjectEmployeeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/projects")
@Tag(
        name = "Projects",
        description = "Endpoints for managing projects and project assignments"
)
public class ProjectController {
    private final ProjectService service;
    private final ProjectRepository projectRepository;


    public ProjectController(ProjectService service, ProjectRepository projectRepository){
        this.service = service;
        this.projectRepository = projectRepository;
    }

    // Creates a new project.
    @PostMapping
    @Operation(summary = "Creates a new project", description = "Creates a new project with lead, customer and optional employees.") // NEU
    @ApiResponse(responseCode = "201", description = "Project successfully created")
       public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody CreateProjectDto dto){
        ProjectDto created = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    // Adds an employee to an existing project.
    @PostMapping("/{projectId}/employees")
    @Operation(summary = "Adds an employee to an existing project", description = "Assigns an employee to a project including their qualification.") // NEU
    @ApiResponse(responseCode = "201", description = "Employee successfully added to project")
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

    // Retrieves all projects.
    @GetMapping
    @Operation(summary = "Retrieves all projects", description = "Returns a list of all available projects.") // NEU
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of projects")
    public ResponseEntity<List<ProjectDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Retrieves a project by its ID
    @GetMapping("/{id}")
    @Operation(summary = "Retrieves a project by its ID", description = "Returns the requested project based on the unique identifier.") // NEU
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the project")
    public ResponseEntity<ProjectDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.findById(id));
    }

    // Retrieves all projects an employee has participated in
    @GetMapping("/employees/{employeeId}")
    @Operation(summary = "Retrieves all projects an employee has participated in", description = "Returns a list of projects associated with the employee, including their qualifications.") // NEU
    @ApiResponse(responseCode = "200", description = "Successfully retrieved employee projects")
    public ResponseEntity<EmployeeProjectsDto> getProjectsByEmployeeId(
            @PathVariable Long employeeId) {

        EmployeeProjectsDto response = service.getProjectsByEmployeeId(employeeId);
        return ResponseEntity.ok(response);
    }

}
