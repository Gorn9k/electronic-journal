package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.entity.common.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "sub_group")
@AttributeOverride(name = "id", column = @Column(name = "sub_group_id"))
public class SubGroup extends AbstractEntity {

    @Column(name = "sub_group_number")
    private Integer subGroupNumber;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "sub_group_type_sub_group", joinColumns = {@JoinColumn(name = "sub_group_id")}, inverseJoinColumns = {
            @JoinColumn(name = "sub_group_type_entity_id")}, uniqueConstraints = {@UniqueConstraint(columnNames = {"sub_group_id", "sub_group_type_entity_id"})})
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private List<SubGroupTypeEntity> subGroupTypeEntities;
}
