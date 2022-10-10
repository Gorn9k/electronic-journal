package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.entity.common.AbstractEntityForRelatedFromSource;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "academic_title")
@AttributeOverride(name = "id", column = @Column(name = "academic_title_id"))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcademicTitle extends AbstractEntityForRelatedFromSource {

    String name;
    Double price;

    @ToString.Exclude
    @OneToMany(mappedBy = "academicTitle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Teacher> teachers;

}
