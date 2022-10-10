package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.repository.AcademicTitleRepository;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.AcademicTitleFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface AcademicTitleService extends CRUDService<AcademicTitleDTO>, RSQLSearch<AcademicTitle, AcademicTitleDTO> {

    List<AcademicTitle> updateFromDekanat(List<AcademicTitleDTO> dto);

    List<AcademicTitleDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress);
}
