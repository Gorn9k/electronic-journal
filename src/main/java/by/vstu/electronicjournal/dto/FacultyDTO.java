package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTORelatedFromSource;
import lombok.Data;

@Data
public class FacultyDTO extends AbstractDTORelatedFromSource {
    private String name;
    private String displayName;
}
