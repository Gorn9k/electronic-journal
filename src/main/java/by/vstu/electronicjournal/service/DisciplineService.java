package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Discipline;
import by.vstu.electronicjournal.repository.DisciplineRepository;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.DisciplineFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface DisciplineService extends CRUDService<DisciplineDTO>, RSQLSearch<Discipline, DisciplineDTO> {

    /**
     * Search changes by params
     *
     * @param query params in RSQL format. Used {@link RSQLSearch}
     * @return list of changes
     */

    List<DisciplineDTO> getDisciplinesByGroup(String query);

    List<Discipline> updateFromDekanat(List<DisciplineDTO> dto);

    List<DisciplineDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress);

}
