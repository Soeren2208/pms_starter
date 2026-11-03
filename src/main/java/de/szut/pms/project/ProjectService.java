package de.szut.pms.project;

import de.szut.pms.customer.CustomerClient;
import de.szut.pms.customer.CustomerNotFoundException;
import de.szut.pms.employee.EmployeeQualificationService;
import de.szut.pms.projectemployee.CreateProjectEmployeeDTO;
import de.szut.pms.projectemployee.ProjectEmployee;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import de.szut.pms.projectemployee.ProjectEmployeeNotFoundException;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;
    private final CustomerClient customerClient;
    private final EmployeeQualificationService qualificationService;

    public ProjectService(ProjectRepository repository, ProjectMapper mapper, CustomerClient customerClient, EmployeeQualificationService qualificationService) {
        this.repository = repository;
        this.mapper = mapper;
        this.customerClient = customerClient;
        this.qualificationService = qualificationService;
    }

    @Transactional
    public ProjectDTO create(CreateProjectDTO dto) {

        // Prüfen, ob der Kunde existiert
        if (!customerClient.existsById(dto.customerId())) {
            throw new CustomerNotFoundException(dto.customerId());
        }

        // Prüfen, ob der Mitarbeiter existiert und die angegebene qualifikation besitzt. Falls Mitarbeiter gesetzt wurden
        if (dto.projectEmployees() != null) {
            for (CreateProjectEmployeeDTO projectEmployee : dto.projectEmployees()) {
                qualificationService.validateQualification(
                        projectEmployee.employeeId(),
                        projectEmployee.qualificationID()
                );
            }
        }

        // Prüfen, ob der Projektleiter existiert
        qualificationService.validateEmployee(dto.responsibleEmployeeId());

        // Projekt erstellen und speichern
        Project saved = repository.save(mapper.toEntity(dto));

        return mapper.toDTO(saved);
    }

    @Transactional
    public ProjectDTO findOneByID(Long id) {
        Project project =  repository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        return mapper.toDTO(project);
    }



    @Transactional(readOnly = true)
    public List<ProjectDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }


    @Transactional
    public ProjectEmployeeRemoveDTO removeEmployee(Long projectId, Long employeeId){
        Project project = repository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException(projectId));

        ProjectEmployee deleteEmployee = null;
        for(ProjectEmployee employee: project.getEmployee()){
            if(employeeId.equals(employee.getEmployeeId())){
                deleteEmployee = employee;
                break;
            }
        }
        if(deleteEmployee != null){
            project.getEmployee().remove(deleteEmployee);
        }else{
            throw new ProjectEmployeeNotFoundException(projectId, employeeId);
        }
        repository.save(project);
        return mapper.toRemoveEmployeeDTO(project);
    }


    @Transactional
    public ProjectEmployeesDTO findAllEmployeesOfProjectOneByID(Long id) {
        Project project =  repository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        return mapper.toProjectEmployeesDTO(project);
    }

}
