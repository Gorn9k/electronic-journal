package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTORelatedFromSource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class DepartmentDTO extends AbstractDTORelatedFromSource {
    private String name;
    private String displayName;
    //private String shortName;
    private FacultyDTO faculty;
    private Long facultyIdFromSource;

}
