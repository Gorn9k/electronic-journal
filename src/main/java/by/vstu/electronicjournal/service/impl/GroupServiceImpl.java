package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.*;
import by.vstu.electronicjournal.entity.*;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.AcademicTitleRepository;
import by.vstu.electronicjournal.repository.DepartmentRepository;
import by.vstu.electronicjournal.repository.GroupRepository;
import by.vstu.electronicjournal.service.*;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.AcademicTitleFactory;
import by.vstu.electronicjournal.service.utils.factory.DepartmentFactory;
import by.vstu.electronicjournal.service.utils.factory.GroupFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import by.vstu.electronicjournal.service.utils.impl.UtilServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl
        extends CommonCRUDServiceImpl<Group, GroupDTO, GroupRepository>
        implements GroupService, UpdateFromRelatedService<Group, GroupDTO, GroupRepository, GroupFactory> {

    @Value("${dekanat}")
    private String path;

    @Autowired
    private Mapper<Group, GroupDTO> mapper;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private ActuatorFromGeneralResources<GroupDTO> relatedResources;

    @Autowired
    private SuperiorService superiorService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private JournalSiteService journalSiteService;

    @Autowired
    private SubGroupService subGroupService;

    public GroupServiceImpl() {
        super(Group.class, GroupDTO.class);
    }

    private AbstractFactoryForRelatedResources<Group, GroupDTO> groupFactory;

    @PostConstruct
    void settingUp() {
        groupFactory = new GroupFactory(departmentRepository);
        relatedResources = new ActuatorFromGeneralResourcesImpl(GroupDTO.class, groupRepository, groupFactory, mapper);
    }

    @Override
    public List<Group> search(String query) {
        if (query.isEmpty()) {
            return groupRepository.findAll();
        } else {
            return groupRepository.findAll(getSpecifications(query));
        }
    }

    @Override
    public List<GroupDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return findAll();
        } else {
            return mapper.toDTOs(groupRepository.findAll(getSpecifications(query)), GroupDTO.class);
        }
    }
/*
    @Override
    public List<GroupDTO> validator(String query) {
        String queryToCommonInfo = String.format("%s/groups/search?q=%s", path, query);
        return relatedResources.findAndAddThings(queryToCommonInfo);
    }

 */

    public List<Group> updateFromDekanat(List<GroupDTO> dto) {
        return defaultUpdateFromRelatedService(dto, groupRepository, (GroupFactory) groupFactory);
    }

    public List<GroupDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress) {
        return defaultGetFromRelatedService(restTemplate, serviceAddress + "/groups?query=", GroupDTO.class);
    }
}
