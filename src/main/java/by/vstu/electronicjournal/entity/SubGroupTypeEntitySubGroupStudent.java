package by.vstu.electronicjournal.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sub_group_type_entity_sub_group_student")
public class SubGroupTypeEntitySubGroupStudent {
    @EmbeddedId
    private SubGroupTypeEntitySubGroupStudentId subGroupTypeEntitySubGroupStudentId = new SubGroupTypeEntitySubGroupStudentId();
    //@ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subGroupTypeEntitySubGroupId")
    @JoinColumns({
            @JoinColumn(name = "sub_group_id"),
            @JoinColumn(name = "sub_group_type_entity_id")
    })
    private SubGroupTypeEntitySubGroup subGroupTypeEntitySubGroup;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;
}
