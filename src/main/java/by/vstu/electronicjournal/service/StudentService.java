package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.StudentDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Student;
import by.vstu.electronicjournal.repository.StudentRepository;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.StudentFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface StudentService extends CRUDService<StudentDTO>, RSQLSearch<Student, StudentDTO> {

    List<StudentDTO> getStudentsByGroup(String query);

    List<Student> updateFromDekanat(List<StudentDTO> dto);

    List<StudentDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress);

    void addGeneralSubGroupOnStudents();
}
