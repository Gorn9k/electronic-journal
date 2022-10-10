package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.AcademicPerformanceDTO;
import by.vstu.electronicjournal.dto.GroupDTO;
import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.StudentPerformanceDTO;
import by.vstu.electronicjournal.service.JournalSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("journal-sites")
public class JournalSiteController {

    @Autowired
    private JournalSiteService journalSiteService;

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("search")
    public List<JournalSiteDTO> search(@RequestParam("q") String query) {
        return journalSiteService.searchAndMapInDTO(query);
    }

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("searchWithoutDublicate")
    public JournalSiteDTO searchWithoutDublicate(@RequestParam("q") String query) {
        return journalSiteService.searchAndMapInDTO(query).get(0);
    }

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("searchByTeacherAndDiscipline")
    public List<GroupDTO> searchByTeacherAndDiscipline(@RequestParam("q") String query) {
        return journalSiteService.searchByTeacherAndDiscipline(query);
    }

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("getAcademicPerformanceByGroupAndDicsipline")
    public List<AcademicPerformanceDTO> getGeneralAcademicPerformance(@RequestParam("q") String query) {
        return journalSiteService.getGeneralAcademicPerformance(query);
    }

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("getAcademicPerformanceStudentById")
    public AcademicPerformanceDTO getStudentOverralGPAById(@RequestParam("q") String query) {
        return journalSiteService.getStudentOverralGPAById(query);
    }

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("getAcademicPerformanceByDisciplineAndStudent")
    public AcademicPerformanceDTO getGeneralStudentProgressInDiscipline(@RequestParam("q") String query) {
        return journalSiteService.getGeneralStudentProgressInDiscipline(query);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public JournalSiteDTO create(@RequestBody JournalSiteDTO dto) {
        return journalSiteService.create(dto);
    }

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("{id}")
    public JournalSiteDTO getById(@PathVariable("id") Long id) {
        return journalSiteService.findOne(id);
    }

    @Secured({"ROLE_USER"})
    @GetMapping("filter")
    public JournalSiteDTO getFilteredByTeacherAndGroupAndDisciplineAndTypeClassAndSubGroup(@RequestParam("teacher_idFromSource") Long teacherIdFromSource,
               @RequestParam("group_name") String groupName, @RequestParam("discipline_id") Long disciplineId, @RequestParam("type_class_id") Long typeClassId,
               @RequestParam("sub_group_number") Integer subGroupNumber) {
        return journalSiteService.getFilteredByTeacherAndGroupAndDisciplineTypeClassAndSubGroup(teacherIdFromSource, groupName, disciplineId, typeClassId, subGroupNumber);
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping("{id}")
    public JournalSiteDTO editById(@PathVariable("id") Long id, @RequestBody JournalSiteDTO dto) {
        return journalSiteService.update(id, dto);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        journalSiteService.deleteById(id);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("deleteByPatternIdFromSource/{pattrentIdFromSource}")
    public void deleteByPattrentIdFromSource(@PathVariable("pattrentIdFromSource") Long pattrentIdFromSource) {
        journalSiteService.deleteByPatternIdFromSource(pattrentIdFromSource);
    }

    @GetMapping("allJournals")
    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN"})
    public Set<JournalSiteDTO> getAllSitesByRole() {
        return journalSiteService.getAllJournals();
    }
}
