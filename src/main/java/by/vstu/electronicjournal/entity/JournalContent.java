package by.vstu.electronicjournal.entity;

import by.vstu.electronicjournal.dto.JournalContentDTO;
import by.vstu.electronicjournal.entity.common.AbstractEntity;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@Table(name = "journal_content")
@AttributeOverride(name = "id", column = @Column(name = "journal_content_id"))
public class JournalContent extends AbstractEntity implements Comparable<JournalContent> {

	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "journal_header_id")
	private JournalHeader journalHeader;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@Column(name = "presence")
	private Boolean presence;

	@Column(name = "lateness")
	private Short lateness;

	@Column(name = "grade")
	private Integer grade;

	@Column(name = "discription")
	private String discription;

	@Override
	public int compareTo(JournalContent o) {
		return (student.getSurname() + " " + student.getName() + " " +
				(student.getPatronymic() == null ? "" : student.getPatronymic())).compareTo(o.student.getSurname() + " " + o.student.getName() +
				(o.student.getPatronymic() == null ? "" : o.student.getPatronymic()));
	}
}
