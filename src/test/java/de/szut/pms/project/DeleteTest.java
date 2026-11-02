package de.szut.pms.project;

import de.szut.pms.projectemployee.ProjectEmployee;
import de.szut.pms.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

public class DeleteTest extends AbstractIntegrationTest {

    @Test
    @WithMockUser
    void deleteRemovesEmployeeFromProject() throws Exception {

          Project project = new Project();
          project.setDescription("Testprojekt");
          project.setResponsibleEmployeeId(1L);
          project.setCustomerId(2L);
          project.setCustomerServiceRepresentative("Max Mustermann");
          project.setComment("Testkommentar");
          project.setStartDate(LocalDate.now());
          project.setEndDate(LocalDate.now().plusDays(10));

          ProjectEmployee employee1 = new ProjectEmployee();
          employee1.setProject(project);
          employee1.setEmployeeId(1L);
          employee1.setQualificationID(2L);
          project.getEmployee().add(employee1);
          ProjectEmployee employee2 = new ProjectEmployee();
          employee2.setProject(project);
          employee2.setEmployeeId(2L);
          employee2.setQualificationID(3L);
          project.getEmployee().add(employee2);
          project = projectRepository.save(project);

          Long IdToBeDeleted = 1L; //der erste Mitarbeiter


        mockMvc.perform(delete("/projects/{projectId}/employees/{employeeId}",
                                project.getId(), IdToBeDeleted).with(csrf())).andExpect(status().isOk());

        Project updatedProject = projectRepository.findById(project.getId()).orElseThrow();

        assertThat(updatedProject.getEmployee()).hasSize(1);

        assertThat(updatedProject.getEmployee().get(0).getEmployeeId()).isEqualTo(2L);


    }

    @Test
    @WithMockUser
    void removeEmployeeFromUnknownProjectReturns404() throws Exception {
        mockMvc.perform(delete("/projects/{projectId}/employees/{employeeId}", 999L, 1L).with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void removeUnknownEmployeeFromProjectReturns404() throws Exception {

        Project project = new Project();
        project.setDescription("Testprojekt");
        project.setResponsibleEmployeeId(1L);
        project.setCustomerId(2L);
        project.setCustomerServiceRepresentative("Max Mustermann");
        project.setComment("Testkommentar");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(10));

        ProjectEmployee employee = new ProjectEmployee();
        employee.setProject(project);
        employee.setEmployeeId(1L);
        employee.setQualificationID(2L);
        project.getEmployee().add(employee);
        project = projectRepository.save(project);

        mockMvc.perform(delete("/projects/{projectId}/employees/{employeeId}",
                        project.getId(),3L).with(csrf())).andExpect(status().isNotFound());
    }

}
