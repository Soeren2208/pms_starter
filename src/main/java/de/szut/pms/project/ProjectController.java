package de.szut.pms.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {this.service = service;}

    @GetMapping("{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findOneByID(id));
    }

}
