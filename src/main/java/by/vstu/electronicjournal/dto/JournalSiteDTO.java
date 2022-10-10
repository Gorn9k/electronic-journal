package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class JournalSiteDTO extends AbstractDTO {

    private DisciplineDTO discipline;
    private TeacherDTO teacher;
    private GroupDTO group;
    private Integer streamId;
    private Long pattentIdFromSource;
    private List<JournalHeaderDTO> journalHeaders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JournalSiteDTO that = (JournalSiteDTO) o;
        return Objects.equals(discipline, that.discipline) &&
                Objects.equals(teacher, that.teacher) &&
                Objects.equals(group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discipline, teacher, group);
    }
}
