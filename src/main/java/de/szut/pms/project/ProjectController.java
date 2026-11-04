package de.szut.pms.project;

import de.szut.pms.client.EmployeeProjectsDto;
import de.szut.pms.projectemployee.CreateProjectEmployeeDto;
import de.szut.pms.projectemployee.ProjectEmployeeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ProblemDetail;
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

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody CreateProjectDto dto){
        ProjectDto created = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

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

    //löscht einen ProjectEmployee Eintrag, der einen MA mit einem Projekt verknüpft
    @DeleteMapping("/{projectId}/employees/{employeeId}")
    @Operation(
            summary = "Remove an employee from a project",
            description = """
        Removes the assignment between an employee and a project.

        Both the project and the employee must exist.
        The employee must currently be assigned to the specified project.

        This operation only deletes the project-employee association.
        The project and employee themselves remain unchanged.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Employee successfully removed from project."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Project, employee or project-employee assignment not found.",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<Void> removeEmployeeFromProject(@PathVariable Long projectId, @PathVariable Long employeeId){
            service.removeEmployeeFromProject(projectId, employeeId);
            return ResponseEntity.noContent().build();
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
