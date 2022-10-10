package by.vstu.electronicjournal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import liquibase.pro.packaged.L;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sub_group_type_sub_group")
public class SubGroupTypeEntitySubGroup {
    @EmbeddedId
    private SubGroupTypeEntitySubGroupId subGroupTypeEntitySubGroupId = new SubGroupTypeEntitySubGroupId();
    //@ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subGroupTypeEntityId")
    @JoinColumn(name = "sub_group_type_entity_id")
    private SubGroupTypeEntity subGroupTypeEntity;
    //@ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subGroupId")
    @JoinColumn(name = "sub_group_id")
    private SubGroup subGroup;
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subGroupTypeEntitySubGroup")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private List<SubGroupTypeEntitySubGroupStudent> subGroupTypeEntitySubGroupStudents;
}
