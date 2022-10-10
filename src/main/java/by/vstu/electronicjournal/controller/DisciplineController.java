package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.StudentDTO;
import by.vstu.electronicjournal.service.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("disciplines")
public class DisciplineController {

    @Autowired
    private DisciplineService disciplineService;

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("search")
    public List<DisciplineDTO> search(@RequestParam("q") String query) {
        return disciplineService.searchAndMapInDTO(query);
    }

    @GetMapping("searchDisciplinesByTeacher")
    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    public List<DisciplineDTO> getDisciplinesByTeacher(@RequestParam("q") String query) {
        return disciplineService.getDisciplinesByGroup(query);
    }
}
