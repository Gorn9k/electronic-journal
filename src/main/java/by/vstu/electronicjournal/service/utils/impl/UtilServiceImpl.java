package by.vstu.electronicjournal.service.utils.impl;

import static java.time.LocalDate.now;

import by.vstu.electronicjournal.dto.JournalHeaderDTO;
import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.requestBodyParams.ContentDTO;
import by.vstu.electronicjournal.dto.requestBodyParams.ParamsForCreateJournalHeader;
import by.vstu.electronicjournal.dto.requestBodyParams.PatternDTO;
import by.vstu.electronicjournal.entity.JournalHeader;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.Teacher;
import by.vstu.electronicjournal.service.*;
import by.vstu.electronicjournal.service.utils.UtilService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@EnableScheduling
public class UtilServiceImpl implements UtilService {

    @Value("${entrance.timetable}")
    private String path;
    private String pathToDekanat = "http://localhost:8080";
    @Value("${PATH_TO_IMAGES}")
    private String pathToImages;
    @Autowired
    private JournalSiteService journalSiteService;
    @Autowired
    private JournalHeaderService journalHeaderService;
    @Autowired
    private JournalContentService journalContentService;
    @Autowired
    private AcademicTitleService academicTitleService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private ForeignLanguageService foreignLanguageService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TypeClassService typeClassService;

    @Scheduled(cron = "0 0 7 * * *")
    @Override
    public void updateFromRelatedServiceEveryDay() {
        long start = System.currentTimeMillis();
        academicTitleService.updateFromDekanat(academicTitleService.getFromDekanat(restTemplate, pathToDekanat));
        facultyService.updateFromDekanat(facultyService.getFromDekanat(restTemplate, pathToDekanat));
        departmentService.updateFromDekanat(departmentService.getFromDekanat(restTemplate, pathToDekanat));
        foreignLanguageService.updateFromDekanat(foreignLanguageService.getFromDekanat(restTemplate, pathToDekanat));
        disciplineService.updateFromDekanat(disciplineService.getFromDekanat(restTemplate, pathToDekanat));
        teacherService.updateFromDekanat(teacherService.getFromDekanat(restTemplate, pathToDekanat));
        groupService.updateFromDekanat(groupService.getFromDekanat(restTemplate, pathToDekanat));
        studentService.updateFromDekanat(studentService.getFromDekanat(restTemplate, pathToDekanat));
        System.out.println("Затраченное время на обновление базы данных журнала в соответствии с базой данных деканата равно: "
                + (System.currentTimeMillis() - start)/1000 + " секунд.");
    }


    @Override
    public void generate() {
        journalContentService.generate(journalHeaderService.generate(journalSiteService.generate()));
    }

    @Override
    public void autoGenerate(List<PatternDTO> patternDTOs) {
        journalContentService.generate(journalHeaderService.generate(journalSiteService.generate(patternDTOs), patternDTOs));
    }

    @Override
    public void uploadImage(String name, MultipartFile file, Long idFromSource) throws IOException {
        name += ".jpg";
        byte[] bytes = file.getBytes();
        BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(new File(pathToImages + name)));
        stream.write(bytes);
        stream.close();
        teacherService.saveTeachersImageByIdFromSource(name, idFromSource);
    }


    @Scheduled(cron = "0 0 7 * * *")
    @Override
    public void generateJournalHeadersEveryDay() {

        List<ContentDTO> usedContentDTOS = new ArrayList<>();

        System.out.println(LocalTime.now());

        for (ContentDTO dto : getContentFromTimetable(now())) {

            List<JournalSite> sites = journalSiteService.search(
                    String.format(
                            "discipline.name==\'%s\';teacher.surname==%s;teacher.name==%s*;teacher.patronymic==%s*;group.name==\'%s\'",
                            dto.getDisciplineName(),
                            dto.getTeacherFio().split(" ")[0],
                            dto.getTeacherFio().split(" ")[1],
                            dto.getTeacherFio().split(" ")[2],
                            dto.getGroupName()
                    )
            );
            for (JournalSite journalSite : sites) {

                boolean flag = false;

                for (JournalHeader journalHeader : journalSite.getJournalHeaders()) {
                    try {
                        if (!journalHeader.getTypeClass().getName().equals(dto.getTypeClassName()) ||
                                !journalHeader.getSubGroup().equals(dto.getSubGroup()) || !journalHeader.getHoursCount().equals(dto.getLessonNumber())
                                || journalHeader.getDateOfLesson().isEqual(now())) {
                            flag = true;
                        }
                    } catch (NullPointerException e) {
                    }

                }

                if (flag) {
                    continue;
                }

                if (usedContentDTOS.isEmpty() || !usedContentDTOS.contains(dto)) {
                    usedContentDTOS.add(dto);
                    ParamsForCreateJournalHeader params = new ParamsForCreateJournalHeader();
                    JournalHeaderDTO journalHeaderDTO = new JournalHeaderDTO();
                    journalHeaderDTO.setHoursCount(dto.getLessonNumber());
                    journalHeaderDTO.setSubGroup(dto.getSubGroup());
                    journalHeaderDTO.setDateOfLesson(dto.getLessonDate());
                    journalHeaderDTO.setTypeClass(
                            typeClassService.validator("name==\'" + dto.getTypeClassName() + "\'").get(0));

                    params.setJournalSiteId(journalSite.getId());
                    params.setJournalHeaderDTO(journalHeaderDTO);

                    journalHeaderService.create(params);
                }
            }
        }
    }

    private LinkedList<ContentDTO> getContentFromTimetable(LocalDate date) {
        String query = String.format("%s/content/search?q=lessonDate==%s",
                path,
                date
        );
        LinkedList<ContentDTO> contentDTOS =
                restTemplate.exchange(query, HttpMethod.GET, null,
                        new ParameterizedTypeReference<LinkedList<ContentDTO>>() {
                        }).getBody();

        for (ContentDTO dto : contentDTOS) {
            if (dto.getChanges() != null) {
                if (!dto.getChanges().getPostponed().isEqual(now())) {
                    contentDTOS.remove(dto);
                    continue;
                }
                if (dto.getChanges().getCanceled() != null && dto.getChanges().getPostponed()
                        .isEqual(null)) {
                    contentDTOS.remove(dto);
                    continue;
                }
            }
        }

        query = String.format("%s/content/search?q=changes.postponed==%s",
                path,
                date
        );

        LinkedList<ContentDTO> postponedContentDTO =
                restTemplate.exchange(query, HttpMethod.GET, null,
                        new ParameterizedTypeReference<LinkedList<ContentDTO>>() {
                        }).getBody();

        contentDTOS.addAll(postponedContentDTO);

        return contentDTOS;
    }

    public static Object getFieldFromAuthentificationDetails(String field) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return ((Map<String, Object>) details.getDecodedDetails()).get(field);
    }
}

