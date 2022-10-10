package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.GroupDTO;
import by.vstu.electronicjournal.entity.Group;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.repository.DepartmentRepository;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import org.springframework.beans.factory.annotation.Autowired;

public final class GroupFactory implements AbstractFactoryForRelatedResources<Group, GroupDTO> {

    private DepartmentRepository departmentRepository;

    public GroupFactory(DepartmentRepository departmentRepository) {this.departmentRepository = departmentRepository;}

    @Override
    public Group create(GroupDTO dto) {
        Group group = new Group();
        group.setName(dto.getName());
        group.setIdFromSource(dto.getIdFromSource());
        group.setStatus(dto.getStatus());
        group.setDepartment(departmentRepository.findByIdFromSource(dto.getDepartmentIdFromSource()));
        return group;
    }

    @Override
    public Group update(Group group, GroupDTO dto) {
        group.setName(dto.getName());
        group.setIdFromSource(dto.getIdFromSource());
        group.setStatus(dto.getStatus());
        group.setDepartment(departmentRepository.findByIdFromSource(dto.getDepartmentIdFromSource()));
        return group;
    }
}
