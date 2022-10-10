package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.entity.common.AbstractEntityForRelatedFromSource;
import by.vstu.electronicjournal.entity.common.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "sub_group_type_entity")
@AttributeOverride(name = "id", column = @Column(name = "sub_group_type_entity_id"))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubGroupTypeEntity extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_group_type")
    private SubGroupType subGroupType;
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "subGroupTypeEntities", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<SubGroup> subGroups;
}
