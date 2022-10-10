package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.entity.common.AbstractEntityForRelatedFromSource;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "faculties")
@AttributeOverride(name = "id", column = @Column(name = "f_id"))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Faculty extends AbstractEntityForRelatedFromSource {

    @Column(name = "f_name")
    String name;

    @Column(name = "f_display_name")
    String displayName;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Department> departments;
}
