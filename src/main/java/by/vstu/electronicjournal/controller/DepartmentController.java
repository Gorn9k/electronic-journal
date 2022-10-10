package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.DepartmentDTO;
import by.vstu.electronicjournal.dto.TypeClassDTO;
import by.vstu.electronicjournal.service.DepartmentService;
import by.vstu.electronicjournal.service.TypeClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("search")
    public List<DepartmentDTO> search(@RequestParam("q") String query) {
        return departmentService.searchAndMapInDTO(query);
    }
}
