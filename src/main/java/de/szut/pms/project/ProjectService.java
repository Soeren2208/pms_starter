package de.szut.pms.project;

import de.szut.pms.customer.CustomerClient;
import de.szut.pms.customer.CustomerNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;
    private final CustomerClient customerClient;

    public ProjectService(ProjectRepository repository, ProjectMapper mapper, CustomerClient customerClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.customerClient = customerClient;
    }

    @Transactional
    public ProjectDTO create(CreateProjectDTO dto) {

        // Prüfen, ob der Kunde existiert
        if (!customerClient.existsById(dto.customerId())) {
            throw new CustomerNotFoundException(dto.customerId());
        }

        // Projekt erstellen und speichern
        Project saved = repository.save(mapper.toEntity(dto));

        return mapper.toDTO(saved);
    }


    @Transactional(readOnly = true)
    public List<ProjectDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

}
