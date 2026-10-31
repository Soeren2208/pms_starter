package de.szut.pms.project;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {this.service = service;}



    @Operation(summary = "Remove an employee from project")
    @ApiResponse(responseCode = "200", description = "Employee successfully removed")
    @ApiResponse(responseCode = "404", description = "Projekt(Id) existiert nicht oder Mitarbeiter ist dem Projekt nicht zugeordnet")
//  @ApiResponse(responseCode = "404", description = "Projekt(Id) existiert nicht")
//  @ApiResponse(responseCode = "404", description = "Mitarbeiter ist dem Projekt nicht zugeordnet")
    @ApiResponse(responseCode = "401", description = "Invalider JWT")
    @DeleteMapping("/{projectId}/employees/{employeeId}")
    public ResponseEntity<ProjectEmployeeRemoveDTO> removeEmployee(@PathVariable Long projectId, @PathVariable Long employeeId){
        ProjectEmployeeRemoveDTO projectEmployeeRemoveDTO = service.removeEmployee(projectId, employeeId);
        return ResponseEntity.ok(projectEmployeeRemoveDTO);
    }


}
