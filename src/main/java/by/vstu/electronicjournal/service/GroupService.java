package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.GroupDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Group;
import by.vstu.electronicjournal.repository.GroupRepository;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.GroupFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface GroupService extends CRUDService<GroupDTO>, RSQLSearch<Group, GroupDTO> {

    List<Group> updateFromDekanat(List<GroupDTO> dto);

    List<GroupDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress);
}
