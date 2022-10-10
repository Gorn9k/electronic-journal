package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.DepartmentDTO;
import by.vstu.electronicjournal.dto.FacultyDTO;
import by.vstu.electronicjournal.dto.GroupDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Department;
import by.vstu.electronicjournal.entity.Faculty;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.AcademicTitleRepository;
import by.vstu.electronicjournal.repository.DepartmentRepository;
import by.vstu.electronicjournal.repository.FacultyRepository;
import by.vstu.electronicjournal.service.AcademicTitleService;
import by.vstu.electronicjournal.service.DepartmentService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.AcademicTitleFactory;
import by.vstu.electronicjournal.service.utils.factory.GroupFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class AcademicTitleServiceImpl extends CommonCRUDServiceImpl<AcademicTitle, AcademicTitleDTO, AcademicTitleRepository>
        implements AcademicTitleService, UpdateFromRelatedService<AcademicTitle, AcademicTitleDTO, AcademicTitleRepository, AcademicTitleFactory> {

    @Value("${dekanat}")
    private String path;

    @Autowired
    private AcademicTitleRepository academicTitleRepository;

    @Autowired
    private Mapper<AcademicTitle, AcademicTitleDTO> mapper;

    private ActuatorFromGeneralResources<AcademicTitleDTO> relatedResources;

    private AbstractFactoryForRelatedResources<AcademicTitle, AcademicTitleDTO> academicTitleFactory;

    public AcademicTitleServiceImpl() {
        super(AcademicTitle.class, AcademicTitleDTO.class);
    }

    @PostConstruct
    void settingUp() {
        academicTitleFactory = new AcademicTitleFactory();
        relatedResources = new ActuatorFromGeneralResourcesImpl(AcademicTitleDTO.class,
                academicTitleRepository, academicTitleFactory, mapper);
    }

    @Override
    public List<AcademicTitle> search(String query) {
        if (query.isEmpty()) {
            return academicTitleRepository.findAll();
        }
        return academicTitleRepository.findAll(getSpecifications(query));
    }

    @Override
    public List<AcademicTitleDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(academicTitleRepository.findAll(getSpecifications(query)), AcademicTitleDTO.class);
    }

    /*@Override
    public List<AcademicTitleDTO> validator(String query) {
        String queryToCommonInfo = String.format("%s/academic_titles/search?q=%s", path, query);
        return relatedResources.findAndAddThings(queryToCommonInfo);
    }

     */

    public List<AcademicTitle> updateFromDekanat(List<AcademicTitleDTO> dto) {
        return defaultUpdateFromRelatedService(dto, academicTitleRepository, (AcademicTitleFactory) academicTitleFactory);
    }

    public List<AcademicTitleDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress) {
        return defaultGetFromRelatedService(restTemplate, serviceAddress + "/academic_titles?query=", AcademicTitleDTO.class);
    }
}
