package by.vstu.electronicjournal.entity.common;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class AbstractEntityForRelatedFromSource extends AbstractEntity{
    @Column(name = "id_from_source")
    private Long idFromSource;
}
