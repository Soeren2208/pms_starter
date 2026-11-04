package de.szut.pms.projectemployee;

import de.szut.pms.common.ResourceNotFoundException;

public class ProjectEmployeeNotFoundException extends ResourceNotFoundException {
    public ProjectEmployeeNotFoundException(Long id){super("There is no ProjectEmployee record with the ID " + id + ".");}
    public ProjectEmployeeNotFoundException(Long projectId, Long employeeId){
        super("In the project with Id " + projectId + " there is no employee with that ID  " + employeeId + ".");
    }
}
