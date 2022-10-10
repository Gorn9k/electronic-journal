package by.vstu.electronicjournal.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
public class SubGroupTypeEntitySubGroupStudentId implements Serializable {
    private SubGroupTypeEntitySubGroupId subGroupTypeEntitySubGroupId;
    private Long studentId;
}
