package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.*;
import by.vstu.electronicjournal.dto.requestBodyParams.ParamsForCreateJournalHeader;
import by.vstu.electronicjournal.dto.requestBodyParams.PatternDTO;
import by.vstu.electronicjournal.entity.*;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.JournalContentRepository;
import by.vstu.electronicjournal.repository.JournalHeaderRepository;
import by.vstu.electronicjournal.repository.JournalSiteRepository;
import by.vstu.electronicjournal.service.*;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import by.vstu.electronicjournal.service.utils.impl.UtilServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JournalHeaderServiceImpl
        extends CommonCRUDServiceImpl<JournalHeader, JournalHeaderDTO, JournalHeaderRepository>
        implements JournalHeaderService {

    @Value("${entrance.timetable}")
    private String path;

    @Autowired
    private Mapper<JournalHeader, JournalHeaderDTO> mapper;

    @Autowired
    private Mapper<Student, StudentDTO> mapperForStudent;

    @Autowired
    private Mapper<JournalContent, JournalContentDTO> mapperForJournalContent;

    @Autowired
    private JournalSiteRepository journalSiteRepository;

    @Autowired
    private JournalHeaderRepository journalHeaderRepository;

    @Autowired
    private JournalContentRepository journalContentRepository;

    @Autowired
    private JournalContentService journalContentService;

    @Autowired
    private TypeClassService typeClassService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SuperiorService superiorService;

    @Autowired
    private TeacherService teacherService;

    public JournalHeaderServiceImpl() {
        super(JournalHeader.class, JournalHeaderDTO.class);
    }

    @Override
    public List<JournalHeader> search(String query) {
        if (query.isEmpty()) {
            return journalHeaderRepository.findAll();
        }
        return journalHeaderRepository.findAll(getSpecifications(query));
    }

    @Override
    public List<JournalHeaderDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(journalHeaderRepository.findAll(getSpecifications(query)), JournalHeaderDTO.class);
    }

    @Override
    public JournalHeaderDTO create(ParamsForCreateJournalHeader params) {

        JournalSite journalSite = journalSiteRepository.getById(params.getJournalSiteId());
        JournalHeader journalHeader = (JournalHeader) mapper
                .toEntity(params.getJournalHeaderDTO(), JournalHeader.class);
        journalHeader.setJournalSite(journalSite);
        journalHeader = journalHeaderRepository.save(journalHeader);

        journalContentService.generate(journalHeader);

        return (JournalHeaderDTO) mapper.toDTO(journalHeader, JournalHeaderDTO.class);
    }

    @Override
    public List<JournalSite> generate(List<JournalSite> params) {

        RestTemplate restTemplate = new RestTemplate();

        List<PatternDTO> patternList = new ArrayList<>();

        for (JournalSite journalSite : params) {

            String queryToCommonInfo = String.format(
                    "%s/patterns/search?q=groupName==%s;disciplineName=='%s';teacherFio==%s*",
                    path,
                    journalSite.getGroup().getName(),
                    journalSite.getDiscipline().getName(),
                    journalSite.getTeacher().getSurname().trim()
            );
            List<PatternDTO> patternDTOS =
                    restTemplate.exchange(queryToCommonInfo, HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<PatternDTO>>() {
                            }).getBody();

            List<JournalHeaderDTO> headerDTOS = new ArrayList<>();

            AtomicBoolean flag = new AtomicBoolean(false);

            for (PatternDTO patternDTO : patternDTOS) {
                flag.set(false);
                if (!headerDTOS.isEmpty()) {
                    break;
                }
                if (patternList.isEmpty()) {
                    patternList.add(patternDTO);
                    JournalHeaderDTO journalHeaderDTO = new JournalHeaderDTO();
                    journalHeaderDTO.setSubGroup(patternDTO.getSubGroup());
                    journalHeaderDTO.setHoursCount(patternDTO.getLessonNumber());
                    journalHeaderDTO.setStatus(Status.ACTIVE);
                    TypeClassDTO typeClassDTO = typeClassService
                            .searchAndMapInDTO("name==\'" + patternDTO.getTypeClassName() + "\'").get(0);
                    journalHeaderDTO.setTypeClass(typeClassDTO);

                    headerDTOS.add(journalHeaderDTO);
                } else {
                    patternList.forEach(patternDTO1 -> {
                        if (patternDTO1.equals(patternDTO)) {
                            flag.set(true);
                        }
                    });
                    if (!flag.get()) {
                        patternList.add(patternDTO);
                        JournalHeaderDTO journalHeaderDTO = new JournalHeaderDTO();
                        journalHeaderDTO.setSubGroup(patternDTO.getSubGroup());
                        journalHeaderDTO.setHoursCount(patternDTO.getLessonNumber());
                        journalHeaderDTO.setStatus(Status.ACTIVE);
                        TypeClassDTO typeClassDTO = typeClassService
                                .searchAndMapInDTO("name==\'" + patternDTO.getTypeClassName() + "\'").get(0);
                        journalHeaderDTO.setTypeClass(typeClassDTO);

                        headerDTOS.add(journalHeaderDTO);
                    }
                }
            }

            if (!headerDTOS.isEmpty()) {
                List<JournalHeader> journalHeaders = mapper.toEntities(headerDTOS, JournalHeader.class);
                journalHeaders.forEach(journalHeader -> journalHeader.setJournalSite(journalSite));
                journalSite.setJournalHeaders(journalHeaderRepository.saveAll(journalHeaders));
            }
        }

        return params;
    }

    @Override
    public List<JournalSite> generate(List<JournalSite> params, List<PatternDTO> patternDTOS) {

        List<PatternDTO> patternList = new ArrayList<>();

        for (JournalSite journalSite : params) {

            List<JournalHeaderDTO> headerDTOS = new ArrayList<>();

            AtomicBoolean flag = new AtomicBoolean(false);

            for (PatternDTO patternDTO : patternDTOS) {
                flag.set(false);
                if (!headerDTOS.isEmpty()) {
                    break;
                }
                if (patternList.isEmpty()) {
                    patternList.add(patternDTO);
                    JournalHeaderDTO journalHeaderDTO = new JournalHeaderDTO();
                    journalHeaderDTO.setSubGroup(patternDTO.getSubGroup());
                    journalHeaderDTO.setHoursCount(patternDTO.getLessonNumber());
                    journalHeaderDTO.setStatus(Status.ACTIVE);
                    TypeClassDTO typeClassDTO = typeClassService
                            .validator("name==\'" + patternDTO.getTypeClassName() + "\'").get(0);
                    journalHeaderDTO.setTypeClass(typeClassDTO);

                    headerDTOS.add(journalHeaderDTO);
                } else {
                    patternList.forEach(patternDTO1 -> {
                        if (patternDTO1.equals(patternDTO)) {
                            flag.set(true);
                        }
                    });
                    if (!flag.get()) {
                        patternList.add(patternDTO);
                        JournalHeaderDTO journalHeaderDTO = new JournalHeaderDTO();
                        journalHeaderDTO.setSubGroup(patternDTO.getSubGroup());
                        journalHeaderDTO.setHoursCount(patternDTO.getLessonNumber());
                        journalHeaderDTO.setStatus(Status.ACTIVE);
                        TypeClassDTO typeClassDTO = typeClassService
                                .validator("name==\'" + patternDTO.getTypeClassName() + "\'").get(0);
                        journalHeaderDTO.setTypeClass(typeClassDTO);

                        headerDTOS.add(journalHeaderDTO);
                    }
                }
            }

            if (!headerDTOS.isEmpty()) {
                List<JournalHeader> journalHeaders = mapper.toEntities(headerDTOS, JournalHeader.class);
                journalHeaders.stream()
                        .forEach(journalHeader -> journalHeader.setJournalSite(journalSite));
                journalSite.setJournalHeaders(journalHeaderRepository.saveAll(journalHeaders));
            }
        }

        return params;
    }

    @Override
    public List<JournalContentDTO> editList(Long id, List<JournalContentDTO> dtos) {

        JournalHeader header = journalHeaderRepository.getById(id);
        List<JournalContent> contents = mapperForJournalContent.toEntities(dtos, JournalContent.class);

        for (JournalContent content : contents) {
            content.setJournalHeader(header);
        }
        return mapperForJournalContent
                .toDTOs(journalContentRepository.saveAllAndFlush(contents), JournalContentDTO.class);
    }

    @Override
    public AcademicPerformanceDTO getTotalNumberMissedClassesByStudentForPeriod(String query) {
        String finalQuery = "";
        Long studentId = Long.parseLong(query.split(";")[0].split("==")[1]);
        Student student = studentService.search("id==" + studentId).get(0);
        List<String> roles = (List<String>) UtilServiceImpl.getFieldFromAuthentificationDetails("roles");
        String field = (String) UtilServiceImpl.getFieldFromAuthentificationDetails("fio");
        List<Superior> superiors = field == null ? new ArrayList<>() : superiorService.search(String.format("fio==\'%s\'", field));
        List<Filter> filters = new ArrayList<>();
        if (!superiors.isEmpty()) {
            filters = superiors.get(0).getFilters();
        }
        List<JournalSite> journalSites = null;
        if (roles == null || roles.contains("HEAD_OF_DEPARTMENT") &&
                filters.stream().noneMatch(filter -> filter.getName().equals(student.getGroup().getDepartment().getName()))||
                roles.contains("DEAN") && filters.stream().noneMatch(filter ->
                        filter.getName().equals(student.getGroup().getDepartment().getFaculty().getName()))) {
            return null;
        }
        if (roles.contains("USER") && !roles.contains("HEAD_OF_DEPARTMENT") && !roles.contains("DEAN") && !roles.contains("RECTOR")) {
            Integer id_from_source = (Integer) UtilServiceImpl.getFieldFromAuthentificationDetails("id_from_source");
            if (id_from_source != null) {
                List<Teacher> teachers = teacherService.search("idFromSource==" + id_from_source);
                Teacher teacher = teachers.isEmpty() ? null : teachers.get(0);
                if (!journalContentService.search(String.format("student.id==%s;journalHeader.journalSite.teacher.id==%s", studentId, teacher.getId())).isEmpty()) {
                    finalQuery = "journalHeader.journalSite.teacher.id==" + teacher.getId() + ";";
                } else {
                    return null;
                }
            }
        } else if (roles.contains("HEAD_OF_DEPARTMENT")) {
            finalQuery = "journalHeader.journalSite.discipline.department.name==" + filters.get(0).getName() + ";";
        } else if (roles.contains("DEAN")) {
            finalQuery = "journalHeader.journalSite.discipline.department.faculty.name==" + filters.get(0).getName() + ";";
        }

        LocalDate after = null, before = null;
        int year = 0, month = 0, dayOfMonth;
        try {
            year = Integer.parseInt(query.split("==")[2].split("and")[0].split("-")[0]);
            month = Integer.parseInt(query.split("==")[2].split("and")[0].split("-")[1]);
            dayOfMonth = Integer.parseInt(query.split("==")[2].split("and")[0].split("-")[2]);
            after = LocalDate.of(year,month,dayOfMonth);
            year = Integer.parseInt(query.split("==")[2].split("and")[1].split("-")[0]);
            month = Integer.parseInt(query.split("==")[2].split("and")[1].split("-")[1]);
            dayOfMonth = Integer.parseInt(query.split("==")[2].split("and")[1].split("-")[2]);
            before = LocalDate.of(year,month,dayOfMonth);
        } catch (Exception e) {
            System.out.println("Incorrect format date!");
        }
        query = String.format("journalHeader.dateOfLesson>=%s and journalHeader.dateOfLesson<=%s;student.id==" + studentId, after, before);
        if (roles.contains("CURATOR")) {
            finalQuery = query;
        } else {
            finalQuery += query;
        }
        List<JournalContent> journalContents = journalContentService.search(finalQuery);
        AcademicPerformanceDTO academicPerformanceDTO = new AcademicPerformanceDTO();
        StudentPerformanceDTO studentPerformanceDTO = new StudentPerformanceDTO();
        studentPerformanceDTO.setStudentDTO(mapperForStudent.toDTO(student, StudentDTO.class));
        if (journalContents.size()!=0) {
            academicPerformanceDTO.setTotalNumberPasses(journalContents.stream().filter(journalContent ->
                    journalContent.getPresence()!=null && journalContent.getPresence().equals(false)).count());
            academicPerformanceDTO.setTotalNumberLates(journalContents.stream().filter(journalContent ->
                    journalContent.getLateness() != null && journalContent.getLateness() != 0).count());
            List<JournalContent> journalContentListFilteredByGrade = journalContents.stream().filter(journalContent ->
                    journalContent.getGrade() != null).collect(Collectors.toList());
            long count = journalContentListFilteredByGrade.size();
            if (count != 0) {
                studentPerformanceDTO.setOverallGPA(journalContentListFilteredByGrade.stream().mapToInt(JournalContent::getGrade).average().getAsDouble());
            }
        }
        academicPerformanceDTO.setStudentPerformanceDTO(studentPerformanceDTO);
        return academicPerformanceDTO;
    }
}
