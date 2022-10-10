package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.DepartmentDTO;
import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.entity.Department;
import by.vstu.electronicjournal.entity.Discipline;
import by.vstu.electronicjournal.repository.FacultyRepository;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import org.springframework.beans.factory.annotation.Autowired;

public final class DepartmentFactory implements AbstractFactoryForRelatedResources<Department, DepartmentDTO> {

    private FacultyRepository facultyRepository;

    public DepartmentFactory(FacultyRepository facultyRepository){this.facultyRepository = facultyRepository;}

    public Department create(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setDisplayName(departmentDTO.getDisplayName());
        department.setIdFromSource(departmentDTO.getIdFromSource());
        department.setStatus(departmentDTO.getStatus());
        department.setFaculty(facultyRepository.findByIdFromSource(departmentDTO.getFacultyIdFromSource()));
        return department;
    }

    public Department update(Department department, DepartmentDTO departmentDTO) {
        department.setDisplayName(departmentDTO.getDisplayName());
        department.setIdFromSource(departmentDTO.getIdFromSource());
        department.setStatus(departmentDTO.getStatus());
        department.setFaculty(facultyRepository.findByIdFromSource(departmentDTO.getFacultyIdFromSource()));
        return department;
    }

}
