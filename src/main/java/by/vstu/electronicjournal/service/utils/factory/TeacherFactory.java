package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.TeacherDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Teacher;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.repository.AcademicTitleRepository;
import by.vstu.electronicjournal.repository.DepartmentRepository;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import org.springframework.beans.factory.annotation.Autowired;

public final class TeacherFactory implements AbstractFactoryForRelatedResources<Teacher, TeacherDTO> {

    private DepartmentRepository departmentRepository;
    private AcademicTitleRepository academicTitleRepository;

    public TeacherFactory(DepartmentRepository departmentRepository, AcademicTitleRepository academicTitleRepository) {
        this.academicTitleRepository = academicTitleRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Teacher create(TeacherDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setIdFromSource(dto.getIdFromSource());
        teacher.setName(dto.getName());
        teacher.setSurname(dto.getSurname());
        teacher.setPatronymic(dto.getPatronymic());
        teacher.setStatus(dto.getStatus());
        teacher.setDepartment(departmentRepository.findByIdFromSource(dto.getDepartmentIdFromSource()));
        teacher.setAcademicTitle(academicTitleRepository.findByIdFromSource(dto.getAcademicTitleIdFromSource()));
        return teacher;
    }

    @Override
    public Teacher update(Teacher teacher, TeacherDTO dto) {
        teacher.setIdFromSource(dto.getIdFromSource());
        teacher.setName(dto.getName());
        teacher.setSurname(dto.getSurname());
        teacher.setPatronymic(dto.getPatronymic());
        teacher.setStatus(dto.getStatus());
        teacher.setDepartment(departmentRepository.findByIdFromSource(dto.getDepartmentIdFromSource()));
        teacher.setAcademicTitle(academicTitleRepository.findByIdFromSource(dto.getAcademicTitleIdFromSource()));
        return teacher;
    }
}
