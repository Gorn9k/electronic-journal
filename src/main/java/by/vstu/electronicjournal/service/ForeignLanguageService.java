package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.ForeignLanguageDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.ForeignLanguage;
import by.vstu.electronicjournal.repository.ForeignLanguageRepository;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.ForeignLanguageFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface ForeignLanguageService extends CRUDService<ForeignLanguageDTO>, RSQLSearch<ForeignLanguage, ForeignLanguageDTO> {

    List<ForeignLanguage> updateFromDekanat(List<ForeignLanguageDTO> dto);

    List<ForeignLanguageDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress);

}
