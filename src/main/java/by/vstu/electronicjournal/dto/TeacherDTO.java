package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTORelatedFromSource;
import lombok.Data;

@Data
public class TeacherDTO extends AbstractDTORelatedFromSource {

    private String surname;
    private String name;
    private String patronymic;
    private DepartmentDTO department;
    private AcademicTitleDTO academicTitle;
    private Long departmentIdFromSource;
    private Long academicTitleIdFromSource;
    private String imageName;
}
