package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.DepartmentDTO;
import by.vstu.electronicjournal.dto.ForeignLanguageDTO;
import by.vstu.electronicjournal.dto.JournalContentDTO;
import by.vstu.electronicjournal.entity.*;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.AcademicTitleRepository;
import by.vstu.electronicjournal.repository.ForeignLanguageRepository;
import by.vstu.electronicjournal.repository.JournalContentRepository;
import by.vstu.electronicjournal.service.ForeignLanguageService;
import by.vstu.electronicjournal.service.JournalContentService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.AcademicTitleFactory;
import by.vstu.electronicjournal.service.utils.factory.DepartmentFactory;
import by.vstu.electronicjournal.service.utils.factory.ForeignLanguageFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ForeignLanguageServiceImpl  extends CommonCRUDServiceImpl<ForeignLanguage, ForeignLanguageDTO, ForeignLanguageRepository>
        implements ForeignLanguageService, UpdateFromRelatedService<ForeignLanguage, ForeignLanguageDTO, ForeignLanguageRepository, ForeignLanguageFactory> {

    @Value("${dekanat}")
    private String path;

    @Autowired
    private Mapper mapper;

    @Autowired
    private ForeignLanguageRepository foreignLanguageRepository;

    public ForeignLanguageServiceImpl() {
        super(ForeignLanguage.class, ForeignLanguageDTO.class);
    }

    private AbstractFactoryForRelatedResources<ForeignLanguage, ForeignLanguageDTO> foreignLanguageFactory;

    private ActuatorFromGeneralResources<ForeignLanguageDTO> relatedResources;

    @PostConstruct
    void settingUp() {
        foreignLanguageFactory = new ForeignLanguageFactory();
        relatedResources = new ActuatorFromGeneralResourcesImpl(ForeignLanguageDTO.class,
                foreignLanguageRepository, foreignLanguageFactory, mapper);
    }

    @Override
    public List<ForeignLanguage> search(String query) {
        if (query.isEmpty()) {
            return foreignLanguageRepository.findAll();
        }
        return foreignLanguageRepository.findAll(getSpecifications(query));
    }

    @Override
    public List<ForeignLanguageDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(foreignLanguageRepository.findAll(getSpecifications(query)), ForeignLanguageDTO.class);
    }
/*
    @Override
    public List<ForeignLanguageDTO> validator(String query) {
        String queryToCommonInfo = String.format("%s/groups/search?q=%s", path, query);
        return relatedResources.findAndAddThings(queryToCommonInfo);
    }

 */

    public List<ForeignLanguage> updateFromDekanat(List<ForeignLanguageDTO> dto) {
        return defaultUpdateFromRelatedService(dto, foreignLanguageRepository, (ForeignLanguageFactory) foreignLanguageFactory);
    }

    public List<ForeignLanguageDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress) {
        return defaultGetFromRelatedService(restTemplate, serviceAddress + "/foreign_languages?query=", ForeignLanguageDTO.class);
    }
}
