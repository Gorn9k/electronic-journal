package by.vstu.electronicjournal.dto.common;

import lombok.Data;
import lombok.ToString;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class AbstractDTORelatedFromSource extends AbstractDTO {
    private Long idFromSource;
}
