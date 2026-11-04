package de.szut.pms.project;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
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
                          "id": 1,
                          "description": "Website-Relaunch",
                          "responsibleEmployeeId": 1,
                          "customerId": 5,
                          "customerServiceRepresentative": "Anna Müller",
                          "comment": "Neuer Webauftritt",
                          "startDate": "2026-11-01",
                          "endDate": "2027-02-28",
                          "projectEmployees": [
                            {
                              "id": 1,
                              "employeeId": 1,
                              "qualificationID": 1
                            },
                            {
                              "id": 2,
                              "employeeId": 2,
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
                    schema = @Schema(implementation = ProblemDetail.class),
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

    @Operation(summary = "Erhalte eine Liste aller Mitarbeiter eines Projektes")
    @ApiResponse(
            responseCode = "200",
            description = "Gesuchtes Projekt mit Mitarbeiter erhalten",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProjectEmployeesDTO.class),
                    examples = @ExampleObject(value = """
                        {
                          "projectId": 1,
                          "description": "Website-Relaunch",
                          "employees": [
                            {
                              "id": 1,
                              "employeeId": 1,
                              "qualificationID": 1
                            },
                            {
                              "id": 2,
                              "employeeId": 2,
                              "qualificationID": 2
                            }
                          ]
                        }
                        """)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Fehler in der Anfrage, möglich das die ID keine Zahl ist",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Projekt nicht gefunden",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(value = """
                        {
                          "detail": "Es gibt keinen Projekteintrag mit der Kennung 99.",
                          "instance": "/projects/99/employees",
                          "status": 404,
                          "title": "Nicht gefunden"
                        }
                        """)
            )
    )
    @GetMapping(value = "{id}/employees", produces = "application/json")
    public ResponseEntity<ProjectEmployeesDTO> getEmployeesFromProject(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findAllEmployeesOfProjectOneByID(id));
    }

    @Operation(summary = "Erhalte ein Projekt für eine bestimmte ID")
    @ApiResponse(
            responseCode = "200",
            description = "Gesuchtes Projekt erhalten",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProjectDTO.class),
                    examples = @ExampleObject(value = """
                        {
                          "id": 1,
                          "description": "Website-Relaunch",
                          "responsibleEmployeeId": 1,
                          "customerId": 5,
                          "customerServiceRepresentative": "Anna Müller",
                          "comment": "Neuer Webauftritt",
                          "startDate": "2026-11-01",
                          "endDate": "2027-02-28",
                          "projectEmployees": [
                            {
                              "id": 1,
                              "employeeId": 1,
                              "qualificationID": 1
                            }
                          ]
                        }
                        """)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Fehler in der Anfrage, möglich das die ID keine Zahl ist",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Projekt nicht gefunden",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(value = """
                        {
                          "detail": "Es gibt keinen Projekteintrag mit der Kennung 99.",
                          "instance": "/projects/99",
                          "status": 404,
                          "title": "Nicht gefunden"
                        }
                        """)
            )
    )
    @GetMapping(value = "{id}", produces = "application/json")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findOneByID(id));
    }

    @Operation(summary = "Erhalte eine Liste aller Projekte")
    @ApiResponse(
            responseCode = "200",
            description = "Projekte erfolgreich abgerufen",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProjectOverviewDTO.class)),
                    examples = @ExampleObject(value = """
                        [
                          {
                            "id": 1,
                            "description": "Website-Relaunch",
                            "responsibleEmployeeId": 1,
                            "customerId": 5,
                            "customerServiceRepresentative": "Anna Müller",
                            "comment": "Neuer Webauftritt",
                            "startDate": "2026-11-01",
                            "endDate": "2027-02-28"
                          },
                          {
                            "id": 2,
                            "description": "CRM-Migration",
                            "responsibleEmployeeId": 3,
                            "customerId": 7,
                            "customerServiceRepresentative": "Jonas Becker",
                            "comment": "Umzug auf neues CRM",
                            "startDate": "2027-01-15",
                            "endDate": "2027-06-30"
                          }
                        ]
                        """)
            )
    )
    @GetMapping
    public ResponseEntity<List<ProjectOverviewDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Ein Projekt löschen")
    @ApiResponse(responseCode = "204", description = "Projekt erfolgreich gelöscht", content = @Content)
    @ApiResponse(
            responseCode = "404",
            description = "Projekt nicht gefunden",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(value = """
                        {
                          "detail": "Es gibt keinen Projekteintrag mit der Kennung 99.",
                          "instance": "/projects/99",
                          "status": 404,
                          "title": "Nicht gefunden"
                        }
                        """)
            )
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build(); //mit .ok() würde 200 returnt
    }

    @Operation(summary = "Einen Mitarbeiter aus dem Projekt entfernen")
    @ApiResponse(
            responseCode = "200",
            description = "Mitarbeiter erfolgreich entfernt",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProjectEmployeeRemoveDTO.class),
                    examples = @ExampleObject(value = """
                        {
                          "id": 1,
                          "description": "Website-Relaunch",
                          "projectEmployees": [
                            {
                              "id": 1,
                              "employeeId": 1,
                              "qualificationID": 1
                            }
                          ]
                        }
                        """)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Das Projekt (ID) existiert nicht oder der Mitarbeiter ist dem Projekt nicht zugeordnet.",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class),
                    examples = @ExampleObject(value = """
                        {
                          "detail": "In dem Projekt mit der Id 1 existiert kein Mitarbeiter mit der Id 2",
                          "instance": "/projects/1/employees/2",
                          "status": 404,
                          "title": "Nicht gefunden"
                        }
                        """)
            )
    )
    @DeleteMapping("/{projectId}/employees/{employeeId}")
    public ResponseEntity<ProjectEmployeeRemoveDTO> removeEmployee(@PathVariable Long projectId, @PathVariable Long employeeId) {
        ProjectEmployeeRemoveDTO projectEmployeeRemoveDTO = service.removeEmployee(projectId, employeeId);
        return ResponseEntity.ok(projectEmployeeRemoveDTO);
    }

}