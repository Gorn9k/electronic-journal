package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.FacultyDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Faculty;
import by.vstu.electronicjournal.repository.FacultyRepository;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.FacultyFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface FacultyService extends CRUDService<FacultyDTO>, RSQLSearch<Faculty, FacultyDTO> {

    List<Faculty> updateFromDekanat(List<FacultyDTO> dto);

    List<FacultyDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress);
}
