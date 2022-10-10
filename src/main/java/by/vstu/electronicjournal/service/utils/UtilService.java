package by.vstu.electronicjournal.service.utils;

import by.vstu.electronicjournal.dto.requestBodyParams.PatternDTO;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UtilService {

    RestTemplate restTemplate = new RestTemplate();

    void generate();

    void autoGenerate(List<PatternDTO> patternDTOs);

    void uploadImage(String name, MultipartFile file, Long idFromSource) throws IOException;

    void generateJournalHeadersEveryDay();

    void updateFromRelatedServiceEveryDay();
}
