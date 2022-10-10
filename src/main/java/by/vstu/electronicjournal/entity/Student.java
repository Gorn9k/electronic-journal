package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.entity.common.AbstractEntityForRelatedFromSource;
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
@Table(name = "student")
@AttributeOverride(name = "id", column = @Column(name = "student_id"))
public class Student extends AbstractEntityForRelatedFromSource implements Comparable<Student>{

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "surname")
    private String surname;

    @Column(name = "name")
    private String name;

    @Column(name = "patronymic")
    private String patronymic;

    @ManyToOne
    @JoinColumn(name = "foreign_language_id")
    private ForeignLanguage foreignLanguage;

    //@ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "student")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private List<SubGroupTypeEntitySubGroupStudent> subGroupTypeEntitySubGroupStudents;

    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "student")
    private List<JournalContent> journalContents = new ArrayList<>();

    @Override
    public int compareTo(Student o) {
        return surname.compareTo(o.getSurname());
    }
}
