package de.szut.pms.project;

import de.szut.pms.projectemployee.ProjectEmployee;
import de.szut.pms.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteTest extends AbstractIntegrationTest {

    @Test
    @WithMockUser
    void deleteRemovesProject() throws Exception {
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
        employee.setEmployeeId(3L);
        employee.setQualificationID(2L);
        project.getEmployee().add(employee);
        project = projectRepository.save(project);

        mockMvc.perform(delete("/projects/{id}",  project.getId()).with(csrf())).andExpect(status().isNoContent());

        assertThat(projectRepository.findById(project.getId())).isEmpty();

    }
}
