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
