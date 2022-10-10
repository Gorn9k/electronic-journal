package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTORelatedFromSource;
import lombok.Data;

@Data
public class GroupDTO extends AbstractDTORelatedFromSource {

    private String name;
    private DepartmentDTO department;
    Long departmentIdFromSource;
}
