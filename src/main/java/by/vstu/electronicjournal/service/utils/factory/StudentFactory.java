package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.StudentDTO;
import by.vstu.electronicjournal.entity.Group;
import by.vstu.electronicjournal.entity.Student;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.repository.ForeignLanguageRepository;
import by.vstu.electronicjournal.repository.GroupRepository;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import org.springframework.beans.factory.annotation.Autowired;

public final class StudentFactory implements AbstractFactoryForRelatedResources<Student, StudentDTO> {

    private ForeignLanguageRepository foreignLanguageRepository;
    private GroupRepository groupRepository;

    public StudentFactory(ForeignLanguageRepository foreignLanguageRepository, GroupRepository groupRepository) {
        this.foreignLanguageRepository = foreignLanguageRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public Student create(StudentDTO dto) {
        Student student = new Student();
        student.setIdFromSource(dto.getIdFromSource());
        student.setName(dto.getName());
        student.setPatronymic(dto.getPatronymic());
        student.setSurname(dto.getSurname());
        student.setStatus(dto.getStatus());
        student.setGroup(groupRepository.findByIdFromSource(dto.getGroupIdFromSource()));
        student.setForeignLanguage(foreignLanguageRepository.findByIdFromSource(dto.getForeignLanguageIdFromSource()));
        return student;
    }

    @Override
    public Student update(Student student, StudentDTO dto) {
        student.setIdFromSource(dto.getIdFromSource());
        student.setName(dto.getName());
        student.setPatronymic(dto.getPatronymic());
        student.setSurname(dto.getSurname());
        student.setStatus(dto.getStatus());
        student.setGroup(groupRepository.findByIdFromSource(dto.getGroupIdFromSource()));
        student.setForeignLanguage(foreignLanguageRepository.findByIdFromSource(dto.getForeignLanguageIdFromSource()));
        return student;
    }
}
