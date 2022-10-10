package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.entity.common.AbstractEntityForRelatedFromSource;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "discipline")
@AttributeOverride(name = "id", column = @Column(name = "discipline_id"))
public class Discipline extends AbstractEntityForRelatedFromSource {

    @Column(name = "short_name")
    private String name;

    //private Group group;

    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "discipline")
    private List<JournalSite> journalSites = new ArrayList<>();

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "d_id")
    private Department department;
}
