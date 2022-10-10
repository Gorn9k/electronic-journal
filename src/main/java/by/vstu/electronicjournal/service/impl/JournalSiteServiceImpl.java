package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.*;
import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.dto.requestBodyParams.PatternDTO;
import by.vstu.electronicjournal.entity.*;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.JournalSiteRepository;
import by.vstu.electronicjournal.service.*;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.impl.UtilServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class JournalSiteServiceImpl
        extends CommonCRUDServiceImpl<JournalSite, JournalSiteDTO, JournalSiteRepository>
        implements JournalSiteService {

    @Value("${entrance.timetable}")
    private String path;

    @Autowired
    private Mapper<JournalSite, JournalSiteDTO> mapper;

    @Autowired
    private Mapper<Student, StudentDTO> mapperForStudent;

    @Autowired
    private Mapper<Group, GroupDTO> mapperForGroup;

    @Autowired
    private Mapper<Discipline, DisciplineDTO> mapperForDiscipline;

    @Autowired
    private Mapper<Teacher, TeacherDTO> mapperForTeacher;

    @Autowired
    private JournalSiteRepository journalSiteRepository;

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private JournalContentService journalContentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private SuperiorService superiorService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubGroupService subGroupService;

    public JournalSiteServiceImpl() {
        super(JournalSite.class, JournalSiteDTO.class);
    }

    @Override
    public List<JournalSite> search(String query) {
        if (query.isEmpty()) {
            return journalSiteRepository.findAll();
        }
        return journalSiteRepository.findAll(getSpecifications(query));
    }

    @Override
    public List<JournalSiteDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(journalSiteRepository.findAll(getSpecifications(query)), JournalSiteDTO.class);
    }

    @Override
    public List<GroupDTO> searchByTeacherAndDiscipline(String query) {
        Set<String> groupSet = new HashSet<>();
        search(query).forEach(journalSite -> groupSet.add(journalSite.getGroup().getName()));
        List<GroupDTO> groupDTOS = new ArrayList<>();
        groupSet.forEach(s -> {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setName(s);
            groupDTOS.add(groupDTO);
        });
        return groupDTOS;
    }

    @Override
    public List<AcademicPerformanceDTO> getGeneralAcademicPerformance(String query) {
        String[] parameters = query.split(";");
        Long disciplineId = Long.parseLong(parameters[0].split("==")[1]);
        String groupName = parameters[1].split("==")[1];
        Group group = groupService.search("name=='" + groupName + "\'").get(0);
        Discipline discipline = disciplineService.search("id==" + disciplineId).get(0);
        List<String> roles = (List<String>) UtilServiceImpl.getFieldFromAuthentificationDetails("roles");
        String field = (String) UtilServiceImpl.getFieldFromAuthentificationDetails("fio");
        List<Superior> superiors = field == null ? new ArrayList<>() : superiorService.search(String.format("fio==\'%s\'", field));
        List<Filter> filters = superiors.isEmpty() ? new ArrayList<>() : superiors.get(0).getFilters();
        List<JournalSite> journalSites = new ArrayList<>();
        if (roles != null && roles.contains("HEAD_OF_DEPARTMENT") &&
                filters.stream().noneMatch(filter -> filter.getName().equals(group.getDepartment().getName())) &&
                filters.stream().noneMatch(filter -> filter.getName().equals(discipline.getDepartment().getName())) ||
                roles.contains("DEAN") && filters.stream().noneMatch(filter -> filter.getName().equals(group.getDepartment().getFaculty().getName())) &&
                        filters.stream().noneMatch(filter -> filter.getName().equals(discipline.getDepartment().getFaculty().getName())) ||
                roles.contains("CURATOR") && filters.stream().noneMatch(filter -> filter.getName().equals(group.getName()))) {
            return null;
        }
        if (roles.contains("USER") && !roles.contains("HEAD_OF_DEPARTMENT") && !roles.contains("DEAN") && !roles.contains("RECTOR")
                && !roles.contains("CURATOR")) {
            Integer id_from_source = (Integer) UtilServiceImpl.getFieldFromAuthentificationDetails("id_from_source");
            if (id_from_source != null) {
                List<Teacher> teachers = teacherService.search("idFromSource==" + id_from_source);
                Teacher teacher = teachers.isEmpty() ? null : teachers.get(0);
                journalSites = search(String.format("group.name=='%s';teacher.id==%s;discipline.id==%s", group.getName(), teacher.getId(), discipline.getId()));
                if (journalSites.isEmpty()) {
                    return null;
                }
            }
        }
        if (journalSites.isEmpty()) {
            journalSites = search(query);
        }
        List<Map<Student, List<JournalContent>>> mapArrayList = new ArrayList<>();
        List<JournalHeader> journalHeaders = getJournalHeadersFromJournalSite(journalSites);
        List<JournalContent> journalContents = new ArrayList<>();
        journalHeaders.forEach(journalHeader -> journalContents.addAll(journalHeader.getJournalContents()));
        Set<Student> hashSet = journalContents.stream().map(JournalContent::getStudent).collect(Collectors.toSet());
        hashSet.forEach(student -> {
            Map<Student, List<JournalContent>> map1 = new HashMap<>();
            map1.put(student, journalContents.stream().filter(journalContent ->
                    journalContent.getStudent().equals(student)).collect(Collectors.toList()));
            mapArrayList.add(map1);
        });

        List<AcademicPerformanceDTO> academicPerformanceDTOList = new ArrayList<>();

        mapArrayList.forEach(studentListMap -> studentListMap.values().forEach(journalContentList ->
                academicPerformanceDTOList.add(getAcademicPerformanceDTOByJournalContentList(journalContentList))));

        return academicPerformanceDTOList;
    }

    private AcademicPerformanceDTO getAcademicPerformanceDTOByJournalContentList(List<JournalContent> journalContentList) {
        AcademicPerformanceDTO academicPerformanceDTO = new AcademicPerformanceDTO();
        if (journalContentList.size() != 0) {
            StudentPerformanceDTO studentPerformanceDTO = new StudentPerformanceDTO();
            studentPerformanceDTO.setStudentDTO(mapperForStudent.toDTO(journalContentList.get(0).getStudent(), StudentDTO.class));
            List<JournalContent> journalContentListFilteredByGrade = journalContentList.stream().filter(journalContent ->
                    journalContent.getGrade() != null).collect(Collectors.toList());
            long count = journalContentListFilteredByGrade.size();
            if (count != 0) {
                studentPerformanceDTO.setOverallGPA(journalContentListFilteredByGrade.stream().mapToInt(JournalContent::getGrade).average().getAsDouble());
            }
            List<JournalContent> journalContentListFilteredByPresence = journalContentList.stream().filter(journalContent ->
                    journalContent.getPresence() != null && journalContent.getPresence().equals(false)).collect(Collectors.toList());
            count = journalContentListFilteredByPresence.size();
            if (count != 0) {
                academicPerformanceDTO.setTotalNumberPasses(count);
            }
            List<JournalContent> journalContentListFilteredByLateness = journalContentList.stream().filter(journalContent ->
                    journalContent.getLateness() != null && journalContent.getLateness() != 0).collect(Collectors.toList());
            count = journalContentListFilteredByLateness.size();
            if (count != 0) {
                academicPerformanceDTO.setTotalNumberLates(count);
            }
            academicPerformanceDTO.setStudentPerformanceDTO(studentPerformanceDTO);
        }
        return academicPerformanceDTO;
    }

    private List<JournalHeader> getJournalHeadersFromJournalSite(List<JournalSite> journalSites) {
        List<JournalHeader> journalHeaders = new ArrayList<>();
        journalSites.forEach(journalSite -> journalHeaders.addAll(journalSite.getJournalHeaders()));
        return journalHeaders;
    }

    @Override
    public AcademicPerformanceDTO getGeneralStudentProgressInDiscipline(String query) {
        List<JournalContent> journalContentList = new ArrayList<>();
        String[] parameters = query.split(";");
        Long disciplineId = Long.parseLong(parameters[0].split("==")[1]);
        Long studentId = Long.parseLong(parameters[1].split("==")[1]);
        Student student = studentService.search("id==" + studentId).get(0);
        Discipline discipline = disciplineService.search("id==" + disciplineId).get(0);
        List<String> roles = (List<String>) UtilServiceImpl.getFieldFromAuthentificationDetails("roles");
        String field = (String) UtilServiceImpl.getFieldFromAuthentificationDetails("fio");
        List<Superior> superiors = field == null ? new ArrayList<>() : superiorService.search(String.format("fio==\'%s\'", field));
        List<Filter> filters = superiors.isEmpty() ? new ArrayList<>() : superiors.get(0).getFilters();
        if (roles != null && roles.contains("HEAD_OF_DEPARTMENT") &&
                filters.stream().noneMatch(filter -> filter.getName().equals(student.getGroup().getDepartment().getName())) &&
                filters.stream().noneMatch(filter -> filter.getName().equals(discipline.getDepartment().getName())) ||
                roles.contains("DEAN") && filters.stream().noneMatch(filter ->
                        filter.getName().equals(student.getGroup().getDepartment().getFaculty().getName())) &&
                filters.stream().noneMatch(filter -> filter.getName().equals(discipline.getDepartment().getFaculty().getName())) ||
                roles.contains("CURATOR") && filters.stream().noneMatch(filter -> filter.getName().equals(student.getGroup().getName()))) {
            return null;
        }
        if (roles.contains("USER") && !roles.contains("HEAD_OF_DEPARTMENT") && !roles.contains("DEAN") &&
                !roles.contains("RECTOR") && !roles.contains("CURATOR")) {
            Integer id_from_source = (Integer) UtilServiceImpl.getFieldFromAuthentificationDetails("id_from_source");
            if (id_from_source != null) {
                List<Teacher> teachers = teacherService.search("idFromSource==" + id_from_source);
                Teacher teacher = teachers.isEmpty() ? null : teachers.get(0);
                journalContentList = journalContentService.search(String.format("student.id==%s;journalHeader.journalSite.discipline.id==%s;" +
                        "journalHeader.journalSite.teacher.id==%s", studentId, disciplineId, teacher.getId()));
                if (journalContentList.isEmpty()) {
                    return null;
                }
            }
        }
        if (journalContentList.isEmpty()) {
            journalContentList = journalContentService.search(String.format("student.id==%s;journalHeader.journalSite.discipline.id==%s",
                    studentId, disciplineId));
        }
        return getAcademicPerformanceDTOByJournalContentList(journalContentList);
    }

    @Override
    public AcademicPerformanceDTO getStudentOverralGPAById(String query) {
        List<JournalContent> journalContents = journalContentService.search(query);
        AcademicPerformanceDTO academicPerformanceDTO = new AcademicPerformanceDTO();
        if (!journalContents.isEmpty()) {
            academicPerformanceDTO = getAcademicPerformanceDTOByJournalContentList(journalContents);
        }
        return academicPerformanceDTO;
    }

    @Override
    public List<JournalSiteDTO> getByDisciplineName(String disciplineName) {
        return mapper.toDTOs(journalSiteRepository.findByDisciplineName(disciplineName), JournalSiteDTO.class);
    }

    @Override
    public void deleteByPatternIdFromSource(Long patternIdFromSource) {
        journalSiteRepository.deleteByPattentIdFromSource(patternIdFromSource);
    }

    @Override
    public List<JournalSite> generate() {

        RestTemplate restTemplate = new RestTemplate();

        String queryToCommonInfo = String.format("%s/patterns/", path);
        List<PatternDTO> patternDTOS =
                restTemplate.exchange(queryToCommonInfo, HttpMethod.GET, null, new ParameterizedTypeReference<List<PatternDTO>>() {
                }).getBody();

        List<JournalSite> result = new ArrayList<>();

        for (PatternDTO patternDTO : patternDTOS) {
            //Discipline discipline = disciplineService.search("name=='" + patternDTO.getDisciplineName() +
            //        "';department.displayName==" + patternDTO.getDepartmentDisciplineDisplayName()).get(0);
            //Teacher teacher = teacherService.search(parsingFIOTeacher(patternDTO.getTeacherFio()) +
            //        ";department.displayName==" + patternDTO.getDepartmentTeacherDisplayName()).get(0);
            //System.out.println(patternDTO.getDisciplineName());
            Discipline discipline = disciplineService.search("name=='" + patternDTO.getDisciplineName() + "'").get(0);
            //System.out.println(patternDTO.getTeacherFio());
            //System.out.println(parsingFIOTeacher(patternDTO.getTeacherFio()));
            Teacher teacher = teacherService.search(parsingFIOTeacher(patternDTO.getTeacherFio())).get(0);
            Group group = groupService.search("name=='" + patternDTO.getGroupName() + "'").get(0);

            JournalSite journalSite = new JournalSite();
            journalSite.setDiscipline(discipline);
            journalSite.setTeacher(teacher);
            journalSite.setGroup(group);
            journalSite.setPattentIdFromSource(patternDTO.getId());
            journalSite.setStatus(Status.ACTIVE);
            result.add(journalSite);
        }

        return journalSiteRepository.saveAll(result);
    }

    @Override
    public List<JournalSite> generate(List<PatternDTO> patternDTOS) {

        List<JournalSite> result = new ArrayList<>();

        for (PatternDTO patternDTO : patternDTOS) {
            //Discipline discipline = disciplineService.search("name=='" + patternDTO.getDisciplineName() +
            //        "';department.displayName==" + patternDTO.getDepartmentDisciplineDisplayName()).get(0);
            //Teacher teacher = teacherService.search(parsingFIOTeacher(patternDTO.getTeacherFio()) +
            //        ";department.displayName==" + patternDTO.getDepartmentTeacherDisplayName()).get(0);
            Discipline discipline = disciplineService.search("name=='" + patternDTO.getDisciplineName()).get(0);
            Teacher teacher = teacherService.search(parsingFIOTeacher(patternDTO.getTeacherFio())).get(0);
            Group group = groupService.search("name=='" + patternDTO.getGroupName() + "'").get(0);

            JournalSite journalSite = new JournalSite();
            journalSite.setDiscipline(discipline);
            journalSite.setTeacher(teacher);
            journalSite.setGroup(group);
            journalSite.setPattentIdFromSource(patternDTO.getId());
            journalSite.setStatus(Status.ACTIVE);
            result.add(journalSite);
        }

        return journalSiteRepository.saveAll(result);
    }

    @Override
    public JournalSiteDTO getFilteredByTeacherAndGroupAndDisciplineTypeClassAndSubGroup(Long teacherIdFromSource, String groupName, Long disciplineId, Long typeClassId, Integer subGroupNumber) {
        List<JournalSite> journalSites = journalSiteRepository.findByTeacherIdFromSourceAndGroupNameAndDisciplineId(teacherIdFromSource, groupName, disciplineId);
        JournalSite journalSite = journalSites.get(0);
        List<JournalHeader> journalHeaders = new ArrayList<>();
        List<JournalHeader> finalJournalHeaders = journalHeaders;
        journalSites.stream().forEach(journalSite1 -> finalJournalHeaders.addAll(journalSite1.getJournalHeaders()));
        journalHeaders = journalHeaders.stream().filter(journalHeader -> journalHeader.getTypeClass().getId().equals(typeClassId) &&
                journalHeader.getSubGroup().equals(subGroupNumber) && journalHeader.getDateOfLesson() != null).collect(Collectors.toList());
        journalSite.setJournalHeaders(journalHeaders);
        return (JournalSiteDTO) mapper.toDTO(journalSite, JournalSiteDTO.class);
    }

    private String parsingFIOTeacher(String fio) {
        fio = fio.replace("'", "").replace(".", " ").replace("  ", " ");
        String[] temp = fio.split(" ");
        return String.format("surname==*%s*;name==*%s*;patronymic==*%s*", temp[0], temp[1], temp[2]);
    }

    @Override
    public Set<JournalSiteDTO> getAllJournals() {

        List<String> roles = (List<String>) UtilServiceImpl.getFieldFromAuthentificationDetails("roles");
        List<JournalSiteDTO> journalSiteDTOS = new ArrayList<>();
        String field = (String) UtilServiceImpl.getFieldFromAuthentificationDetails("fio");
        List<Superior> superiors;
        if (roles != null && roles.contains("RECTOR") || roles.contains("ADMIN")) {
            journalSiteDTOS = searchAndMapInDTO("");
        } else if (roles != null && field != null && roles.contains("HEAD_OF_DEPARTMENT")) {
            superiors = superiorService.search(String.format("fio==\'%s\'", field));
            journalSiteDTOS = searchAndMapInDTO(String.format("teacher.department.name==\'%s\'", superiors.isEmpty() ? "" : superiors.get(0).getFilters().get(0)));
        } else if (roles != null && field != null && roles.contains("DEAN")) {
            superiors = superiorService.search(String.format("fio==\'%s\'", field));
            journalSiteDTOS = searchAndMapInDTO(String.format("teacher.department.faculty.name==\'%s\'", superiors.isEmpty() ? "" : superiors.get(0).getFilters().get(0)));
        }
        journalSiteDTOS.forEach(journalSiteDTO -> journalSiteDTO.setJournalHeaders(null));
        Set<JournalSiteDTO> siteDTOS = new HashSet<>(journalSiteDTOS);
        return siteDTOS;
    }
}
