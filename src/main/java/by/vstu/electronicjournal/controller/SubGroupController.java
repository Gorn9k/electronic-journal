package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.SubGroupDTO;
import by.vstu.electronicjournal.service.SubGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("sub_groups")
public class SubGroupController {

    @Autowired
    private SubGroupService subGroupService;

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("search")
    public List<SubGroupDTO> search(@RequestParam("q") String query) {
        return subGroupService.searchAndMapInDTO(query);
    }
}
