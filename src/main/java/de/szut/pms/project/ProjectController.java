package de.szut.pms.project;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {this.service = service;}

    @ApiResponse(responseCode = "200", description = "Gesuchtes Projekt mit Mitarbeiter erhalten")
    @ApiResponse(responseCode = "400", description = "Fehler in der Anfrage, möglich das die ID keine Zahl ist",content = @Content(mediaType = "application/problem+json"))
    @ApiResponse(responseCode = "404", description = "Projekt nicht gefunden",content = @Content(mediaType = "application/problem+json"))
    @GetMapping(value = "{id}/employees", produces = "application/json")
    public ResponseEntity<ProjectEmployeesDTO> getEmployessFromProject(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findAllEmployeesOfProjectOneByID(id));
    }

}
