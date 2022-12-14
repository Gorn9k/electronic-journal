package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.TypeClassDTO;
import by.vstu.electronicjournal.service.TypeClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("type-classes")
public class TypeClassController {

    @Autowired
    private TypeClassService typeClassService;

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("search")
    public List<TypeClassDTO> search(@RequestParam("q") String query) {
        return typeClassService.searchAndMapInDTO(query);
    }
}
