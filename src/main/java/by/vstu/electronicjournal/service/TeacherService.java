package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.TeacherDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Teacher;
import by.vstu.electronicjournal.repository.TeacherRepository;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.TeacherFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface TeacherService extends CRUDService<TeacherDTO>, RSQLSearch<Teacher, TeacherDTO> {

    List<Teacher> updateFromDekanat(List<TeacherDTO> dto);

    List<TeacherDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress);

    void saveTeachersImageByIdFromSource(String imageName, Long idFromSource);
}
