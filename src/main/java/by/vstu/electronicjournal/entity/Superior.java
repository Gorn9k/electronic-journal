package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "superior")
@AttributeOverride(name = "id", column = @Column(name = "superior_id"))
public class Superior extends AbstractEntity {

    @Column(name = "fio")
    private String fio;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, mappedBy = "superior")
    private List<Filter> filters = new ArrayList<>();

}
