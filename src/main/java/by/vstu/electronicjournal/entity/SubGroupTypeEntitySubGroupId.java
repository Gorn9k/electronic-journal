package by.vstu.electronicjournal.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
public class SubGroupTypeEntitySubGroupId implements Serializable {
    private Long subGroupTypeEntityId;
    private Long subGroupId;
}
