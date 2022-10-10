package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.*;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Department;
import by.vstu.electronicjournal.entity.Discipline;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.AcademicTitleRepository;
import by.vstu.electronicjournal.repository.DepartmentRepository;
import by.vstu.electronicjournal.repository.DisciplineRepository;
import by.vstu.electronicjournal.service.DisciplineService;
import by.vstu.electronicjournal.service.JournalSiteService;
import by.vstu.electronicjournal.service.SuperiorService;
import by.vstu.electronicjournal.service.TeacherService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.AcademicTitleFactory;
import by.vstu.electronicjournal.service.utils.factory.DepartmentFactory;
import by.vstu.electronicjournal.service.utils.factory.DisciplineFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import by.vstu.electronicjournal.service.utils.impl.UtilServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DisciplineServiceImpl
        extends CommonCRUDServiceImpl<Discipline, DisciplineDTO, DisciplineRepository>
        implements DisciplineService, UpdateFromRelatedService<Discipline, DisciplineDTO, DisciplineRepository, DisciplineFactory> {

    @Value("${dekanat}")
    private String path;

    @Autowired
    private Mapper<Discipline, DisciplineDTO> mapper;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private JournalSiteService journalSiteService;

    private ActuatorFromGeneralResources<DisciplineDTO> relatedResources;

    public DisciplineServiceImpl() {
        super(Discipline.class, DisciplineDTO.class);
    }

    private AbstractFactoryForRelatedResources<Discipline, DisciplineDTO> disciplineFactory;

    @PostConstruct
    void settingUp() {
        disciplineFactory = new DisciplineFactory(departmentRepository);
        relatedResources = new ActuatorFromGeneralResourcesImpl(DisciplineDTO.class, disciplineRepository, disciplineFactory, mapper);
    }

    @Override
    public List<Discipline> search(String query) {
        if (query.isEmpty()) {
            return disciplineRepository.findAll();
        }
        return disciplineRepository.findAll(getSpecifications(query));
    }

    @Override
    public List<DisciplineDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(disciplineRepository.findAll(getSpecifications(query)), DisciplineDTO.class);
    }

    @Override
    public List<DisciplineDTO> getDisciplinesByGroup(String query) {
        Set<Long> set = new HashSet<>();
        journalSiteService.search(query).forEach(journalSite -> set.add(journalSite.getDiscipline().getId()));

        List<Discipline> disciplines = new ArrayList<>();
        set.forEach(s -> {
            Discipline discipline = disciplineRepository.getById(s);
            disciplines.add(discipline);
        });

        return mapper.toDTOs(disciplines, DisciplineDTO.class);
    }
/*
    @Override
    public List<DisciplineDTO> validator(String query) {
        String queryToCommonInfo = String.format("%s/disciplines/search?q=%s", path, query);
        return relatedResources.findAndAddThings(queryToCommonInfo);
    }

 */

    public List<Discipline> updateFromDekanat(List<DisciplineDTO> dto) {
        return defaultUpdateFromRelatedService(dto, disciplineRepository, (DisciplineFactory) disciplineFactory);
    }

    public List<DisciplineDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress) {
        return defaultGetFromRelatedService(restTemplate, serviceAddress + "/disciplines?query=", DisciplineDTO.class);
    }
}
