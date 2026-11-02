package de.szut.pms.project;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {this.service = service;}

    @ApiResponse(responseCode = "200", description = "Gesuchtes Projekt erhalten")
    @ApiResponse(responseCode = "400", description = "Fehler in der Anfrage, möglich das die ID keine Zahl ist",content = @Content(mediaType = "application/problem+json"))
    @ApiResponse(responseCode = "404", description = "Projekt nicht gefunden",content = @Content(mediaType = "application/problem+json"))
    @GetMapping(value = "{id}", produces = "application/json")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findOneByID(id));
    }

}
