/*package de.szut.pms.projectemployee;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/projectEmployees")
public class ProjectEmployeeController {
    private final ProjectEmployeeService service;

    public ProjectEmployeeController(ProjectEmployeeService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<ProjectEmployeeDto> createProjectEmployee(@Valid @RequestBody CreateProjectEmployeeDto dto){
        ProjectEmployeeDto created = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectEmployeeDto> findProjectEmployeeById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
}*/
