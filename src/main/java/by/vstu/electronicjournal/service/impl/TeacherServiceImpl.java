package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.DepartmentDTO;
import by.vstu.electronicjournal.dto.TeacherDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Department;
import by.vstu.electronicjournal.entity.Filter;
import by.vstu.electronicjournal.entity.Teacher;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.AcademicTitleRepository;
import by.vstu.electronicjournal.repository.DepartmentRepository;
import by.vstu.electronicjournal.repository.ForeignLanguageRepository;
import by.vstu.electronicjournal.repository.TeacherRepository;
import by.vstu.electronicjournal.service.SuperiorService;
import by.vstu.electronicjournal.service.TeacherService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.AcademicTitleFactory;
import by.vstu.electronicjournal.service.utils.factory.DepartmentFactory;
import by.vstu.electronicjournal.service.utils.factory.TeacherFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import by.vstu.electronicjournal.service.utils.impl.UtilServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl
        extends CommonCRUDServiceImpl<Teacher, TeacherDTO, TeacherRepository>
        implements TeacherService, UpdateFromRelatedService<Teacher, TeacherDTO, TeacherRepository, TeacherFactory> {

    @Value("${dekanat}")
    private String path;

    @Autowired
    private Mapper<Teacher, TeacherDTO> mapper;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AcademicTitleRepository academicTitleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SuperiorService superiorService;

    private ActuatorFromGeneralResources<TeacherDTO> relatedResources;

    public TeacherServiceImpl() {
        super(Teacher.class, TeacherDTO.class);
    }

    private AbstractFactoryForRelatedResources<Teacher, TeacherDTO> teacherFactory;

    @PostConstruct
    void settingUp() {
        teacherFactory = new TeacherFactory(departmentRepository, academicTitleRepository);
        relatedResources = new ActuatorFromGeneralResourcesImpl(TeacherDTO.class, teacherRepository, teacherFactory, mapper);
    }

    @Override
    public List<Teacher> search(String query) {
        if (query.isEmpty()) {
            return teacherRepository.findAll();
        }
        return teacherRepository.findAll(getSpecifications(query));
    }

    @Override
    public List<TeacherDTO> searchAndMapInDTO(String query) {
        List<String> roles = (List<String>) UtilServiceImpl.getFieldFromAuthentificationDetails("roles");
        List<Filter> filters = null;
        String field = null;
        Integer id_from_source = (Integer) UtilServiceImpl.getFieldFromAuthentificationDetails("id_from_source");
        try {
            field = (String) UtilServiceImpl.getFieldFromAuthentificationDetails("fio");
            filters = superiorService.search(String.format("fio==\'%s\'", field)).get(0).getFilters();
        } catch (Exception ignored) {}
        List<Teacher> teachers = new ArrayList<>();
        teachers = search(query);
        if (roles != null && (roles.contains("RECTOR") || roles.contains("ADMIN"))) {
            return mapper.toDTOs(teachers, TeacherDTO.class);
        } else if (roles != null && roles.contains("HEAD_OF_DEPARTMENT")) {
            List<Filter> finalFilters = filters;
            return mapper.toDTOs(teachers.stream().filter(teacherDTO -> finalFilters.stream().anyMatch(filter -> teacherDTO.getDepartment() != null &&
                    filter.getName().equals(teacherDTO.getDepartment().getName()))).collect(Collectors.toList()), TeacherDTO.class);
        } else if (roles != null && roles.contains("DEAN")) {
            List<Filter> finalFilters1 = filters;
            return mapper.toDTOs(teachers.stream().filter(teacherDTO -> finalFilters1.stream().anyMatch(filter ->
                    teacherDTO.getDepartment() != null && teacherDTO.getDepartment().getFaculty() != null &&
                            filter.getName().equals(teacherDTO.getDepartment().getFaculty().getName()))).collect(Collectors.toList()), TeacherDTO.class);
        } else if (roles != null && roles.contains("USER")){
            return mapper.toDTOs(teachers.stream().filter(teacher ->
                    teacher.getIdFromSource().equals(Long.parseLong(Integer.toString(id_from_source)))).collect(Collectors.toList()), TeacherDTO.class);
        }
        return null;
    }
/*
    @Override
    public List<TeacherDTO> validator(String query) {
        String queryToCommonInfo = String.format("%s/employees/search?q=%s", path, query);
        return relatedResources.findAndAddThings(queryToCommonInfo);
    }

 */

    public List<Teacher> updateFromDekanat(List<TeacherDTO> dto) {
        return defaultUpdateFromRelatedService(dto, teacherRepository, (TeacherFactory) teacherFactory);
    }

    public List<TeacherDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress) {
        return defaultGetFromRelatedService(restTemplate, serviceAddress + "/teachers?query=", TeacherDTO.class);
    }

    @Override
    public void saveTeachersImageByIdFromSource(String imageName, Long idFromSource) {
        Teacher teacher = search("id_from_source==" + idFromSource).get(0);
        teacher.setImageName(imageName);
        teacherRepository.save(teacher);
    }
}
