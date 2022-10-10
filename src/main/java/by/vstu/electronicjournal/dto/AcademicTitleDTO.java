package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTORelatedFromSource;
import lombok.Data;

@Data
public class AcademicTitleDTO extends AbstractDTORelatedFromSource {

    private String name;
    private Double price;
}
