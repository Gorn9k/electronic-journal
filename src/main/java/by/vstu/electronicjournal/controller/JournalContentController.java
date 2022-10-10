package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.AcademicPerformanceDTO;
import by.vstu.electronicjournal.dto.JournalContentDTO;
import by.vstu.electronicjournal.dto.StudentPerformanceDTO;
import by.vstu.electronicjournal.service.JournalContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("journal-contents")
public class JournalContentController {

    @Autowired
    private JournalContentService journalContentService;

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("search")
    public List<JournalContentDTO> search(@RequestParam("q") String query) {
        return journalContentService.searchAndMapInDTO(query);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public JournalContentDTO create(@RequestBody JournalContentDTO contentDTO) {
        return journalContentService.create(contentDTO);
    }

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("{id}")
    public JournalContentDTO getById(@PathVariable("id") Long id) {
        return journalContentService.findOne(id);
    }

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @PatchMapping("{id}")
    public JournalContentDTO editById(@PathVariable("id") Long id, @RequestBody JournalContentDTO contentDTO) {
        return journalContentService.update(id, contentDTO);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        journalContentService.deleteById(id);
    }
}
