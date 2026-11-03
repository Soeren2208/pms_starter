package de.szut.pms.project;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }


    @Operation(summary = "Lege ein neues Projekt an")
    @ApiResponse(
            responseCode = "201",
            description = "Projekt wurde angelegt",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProjectDTO.class),
                    examples = @ExampleObject(value = """
                        {
                          "id": 7,
                          "description": "Website-Relaunch",
                          "responsibleEmployeeId": 12,
                          "customerId": 5,
                          "customerServiceRepresentative": "Anna Müller",
                          "comment": "Neuer Webauftritt",
                          "startDate": "2026-11-01",
                          "endDate": "2027-02-28",
                          "projectEmployees": [
                            {
                              "id": 1,
                              "employeeId": 12,
                              "qualificationID": 3
                            },
                            {
                              "id": 2,
                              "employeeId": 15,
                              "qualificationID": 2
                            }
                          ]
                        }
                        """)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Der Rumpf war fehlerhaft",
            content = @Content(
                    mediaType = "application/problem+json",
                    examples = @ExampleObject(value = """
            {
              "detail": "Die Anfrage enthält ungültige Felder.",
              "instance": "/projects",
              "status": 400,
              "title": "Ungültige Eingabe",
              "errors": {
                "startDate": "Das Startdatum muss angegeben werden."
              }
            }
            """)
            )
    )
    @PostMapping
    public ResponseEntity<ProjectDTO> create(@RequestBody @Valid CreateProjectDTO dto) {
        ProjectDTO created = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }


    @Operation(summary = "Erhalte eine Liste aller Projekte")
    @ApiResponse(responseCode = "200", description = "Projekte erfolgreich abgerufen")
    @ApiResponse(responseCode = "500", description = "Interner Serverfehler")
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Delete a project")
    @ApiResponse(responseCode = "204", description = "Project successfully deleted")
    @ApiResponse(responseCode = "404", description = "Project not found")
    @ApiResponse(responseCode = "401", description = "Invalid JWT")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build(); //mit .ok() würde 200 returnt
    }
}
