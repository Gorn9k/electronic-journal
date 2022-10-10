package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.DepartmentDTO;
import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.dto.FacultyDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Department;
import by.vstu.electronicjournal.entity.Discipline;
import by.vstu.electronicjournal.entity.Faculty;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.AcademicTitleRepository;
import by.vstu.electronicjournal.repository.DisciplineRepository;
import by.vstu.electronicjournal.repository.FacultyRepository;
import by.vstu.electronicjournal.service.DisciplineService;
import by.vstu.electronicjournal.service.FacultyService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.AcademicTitleFactory;
import by.vstu.electronicjournal.service.utils.factory.DepartmentFactory;
import by.vstu.electronicjournal.service.utils.factory.FacultyFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class FacultyServiceImpl extends CommonCRUDServiceImpl<Faculty, FacultyDTO, FacultyRepository>
        implements FacultyService, UpdateFromRelatedService<Faculty, FacultyDTO, FacultyRepository, FacultyFactory> {

    @Value("${dekanat}")
    private String path;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private Mapper<Faculty, FacultyDTO> mapper;

    public FacultyServiceImpl() {
        super(Faculty.class, FacultyDTO.class);
    }

    private AbstractFactoryForRelatedResources<Faculty, FacultyDTO> facultyFactory;

    private ActuatorFromGeneralResources<FacultyDTO> relatedResources;

    @PostConstruct
    void settingUp() {
        facultyFactory = new FacultyFactory();
        relatedResources = new ActuatorFromGeneralResourcesImpl(FacultyDTO.class,
                facultyRepository, facultyFactory, mapper);
    }

    @Override
    public List<Faculty> search(String query) {
        if (query.isEmpty()) {
            return facultyRepository.findAll();
        }
        return facultyRepository.findAll(getSpecifications(query));
    }

    @Override
    public List<FacultyDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(facultyRepository.findAll(getSpecifications(query)), FacultyDTO.class);
    }
/*
    @Override
    public List<FacultyDTO> validator(String query) {
        String queryToCommonInfo = String.format("%s/groups/search?q=%s", path, query);
        return relatedResources.findAndAddThings(queryToCommonInfo);
    }

 */

    public List<Faculty> updateFromDekanat(List<FacultyDTO> dto) {
        return defaultUpdateFromRelatedService(dto, facultyRepository, (FacultyFactory) facultyFactory);
    }

    public List<FacultyDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress) {
        return defaultGetFromRelatedService(restTemplate, serviceAddress + "/faculties?query=", FacultyDTO.class);
    }
}
