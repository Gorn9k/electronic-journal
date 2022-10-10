package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.entity.SubGroupType;
import lombok.Data;

@Data
public class SubGroupDTO extends AbstractDTO {

    private Integer subGroupNumber;

    private SubGroupType subGroupType;
}
