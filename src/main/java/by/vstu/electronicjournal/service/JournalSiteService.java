package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.*;
import by.vstu.electronicjournal.dto.requestBodyParams.PatternDTO;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface JournalSiteService extends CRUDService<JournalSiteDTO>, RSQLSearch<JournalSite, JournalSiteDTO> {

    /**
     * Generate journal for employees.
     * This method must be run after create content in timetable app.
     * Right now, this method start if it was called.
     * In future, it must be run automatically
     *
     * @deprecated
     */
    List<JournalSite> generate();

    List<JournalSite> generate(List<PatternDTO> patternDTOS);

    JournalSiteDTO getFilteredByTeacherAndGroupAndDisciplineTypeClassAndSubGroup(Long teacherIdFromSource, String groupName, Long disciplineId, Long typeClassId,
                                                                                 Integer subGroupNumber);

    List<GroupDTO> searchByTeacherAndDiscipline(String query);

    List<AcademicPerformanceDTO> getGeneralAcademicPerformance(String query);

    AcademicPerformanceDTO getGeneralStudentProgressInDiscipline(String query);

    AcademicPerformanceDTO getStudentOverralGPAById(String query);

    List<JournalSiteDTO> getByDisciplineName(String disciplineName);

    void deleteByPatternIdFromSource(Long patternIdFromSource);

    Set<JournalSiteDTO> getAllJournals();

}
