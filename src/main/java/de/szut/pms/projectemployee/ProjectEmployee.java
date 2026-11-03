package de.szut.pms.projectemployee;

import de.szut.pms.project.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="projectemployee")
@Getter
@Setter
@NoArgsConstructor
public class ProjectEmployee {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="project_id", nullable = false)
    private Project project;

    private Long employeeId;

    private Long qualificationID;
}
