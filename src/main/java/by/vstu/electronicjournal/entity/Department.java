package by.vstu.electronicjournal.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.entity.common.AbstractEntityForRelatedFromSource;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@RequiredArgsConstructor
@Table(name = "departments")
@AttributeOverride(name = "id", column = @Column(name = "d_id"))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Department extends AbstractEntityForRelatedFromSource {

    @Column(name = "d_name")
    String name;

    @Column(name = "d_display_name")
    String displayName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "f_id")
    Faculty faculty;

    @ToString.Exclude
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Teacher> teachers;

    @ToString.Exclude
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Group> groups;

    @ToString.Exclude
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Discipline> disciplines;
}
