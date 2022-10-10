package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.entity.Discipline;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.repository.DepartmentRepository;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import org.springframework.beans.factory.annotation.Autowired;

public final class DisciplineFactory implements AbstractFactoryForRelatedResources<Discipline, DisciplineDTO> {

    private DepartmentRepository departmentRepository;

    public DisciplineFactory(DepartmentRepository departmentRepository) {this.departmentRepository = departmentRepository;}

    public Discipline create(DisciplineDTO disciplineDTO) {
        Discipline discipline = new Discipline();
        discipline.setName(disciplineDTO.getName());
        discipline.setIdFromSource(disciplineDTO.getIdFromSource());
        discipline.setStatus(disciplineDTO.getStatus());
        discipline.setDepartment(departmentRepository.findByIdFromSource(disciplineDTO.getDepartmentIdFromSource()));
        return discipline;
    }

    public Discipline update(Discipline discipline, DisciplineDTO disciplineDTO) {
        discipline.setName(disciplineDTO.getName());
        discipline.setIdFromSource(disciplineDTO.getIdFromSource());
        discipline.setStatus(disciplineDTO.getStatus());
        discipline.setDepartment(departmentRepository.findByIdFromSource(disciplineDTO.getDepartmentIdFromSource()));
        return discipline;
    }
}

