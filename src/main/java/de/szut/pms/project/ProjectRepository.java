package de.szut.pms.project;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByProjectNameAndCustomerId(
            String projectName,
            Long customerId
    );
}
