package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.entity.common.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "journal_site")
@AttributeOverride(name = "id", column = @Column(name = "journal_site_id"))
public class JournalSite extends AbstractEntity {

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "stream_id")
    private Integer streamId;

    @Column(name = "pattent_id_from_source")
    private Long pattentIdFromSource;

    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, mappedBy = "journalSite")
    private List<JournalHeader> journalHeaders = new ArrayList<>();
}
