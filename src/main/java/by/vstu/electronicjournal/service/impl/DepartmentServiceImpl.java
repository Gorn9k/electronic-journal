package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.DepartmentDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Department;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.AcademicTitleRepository;
import by.vstu.electronicjournal.repository.DepartmentRepository;
import by.vstu.electronicjournal.repository.FacultyRepository;
import by.vstu.electronicjournal.service.DepartmentService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.AcademicTitleFactory;
import by.vstu.electronicjournal.service.utils.factory.DepartmentFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl extends CommonCRUDServiceImpl<Department, DepartmentDTO, DepartmentRepository>
        implements DepartmentService, UpdateFromRelatedService<Department, DepartmentDTO, DepartmentRepository, DepartmentFactory> {

    @Value("${dekanat}")
    private String path;

    private ActuatorFromGeneralResources<DepartmentDTO> relatedResources;

    @Autowired
    private Mapper<Department, DepartmentDTO> mapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private AbstractFactoryForRelatedResources<Department, DepartmentDTO> departmentFactory;

    public DepartmentServiceImpl() {
        super(Department.class, DepartmentDTO.class);
    }

    @PostConstruct
    void settingUp() {
        departmentFactory = new DepartmentFactory(facultyRepository);
        relatedResources = new ActuatorFromGeneralResourcesImpl(DepartmentDTO.class,
                departmentRepository, departmentFactory, mapper);
    }

    @Override
    public List<Department> search(String query) {
        if (query.isEmpty()) {
            return departmentRepository.findAll();
        }
        return departmentRepository.findAll(getSpecifications(query));
    }

    @Override
    public List<DepartmentDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(departmentRepository.findAll(getSpecifications(query)), DepartmentDTO.class);
    }
/*
    @Override
    public List<DepartmentDTO> validator(String query) {
        String queryToCommonInfo = String.format("%s/groups/search?q=%s", path, query);
        return relatedResources.findAndAddThings(queryToCommonInfo);
    }

 */

    public List<Department> updateFromDekanat(List<DepartmentDTO> dto) {
        return defaultUpdateFromRelatedService(dto, departmentRepository, (DepartmentFactory) departmentFactory);
    }

    public List<DepartmentDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress) {
        return defaultGetFromRelatedService(restTemplate, serviceAddress + "/departments?query=", DepartmentDTO.class);
    }
}
