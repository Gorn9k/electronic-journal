package by.vstu.electronicjournal.dto;

import by.vstu.electronicjournal.dto.common.AbstractDTORelatedFromSource;
import lombok.Data;

import java.util.List;


//@EqualsAndHashCode(callSuper = false)
@Data
public class StudentDTO extends AbstractDTORelatedFromSource {

    private String surname;
    private String name;
    private String patronymic;
    //private String groupName;
    //private Byte subGroupIdentificator;
    private List<SubGroupDTO> subGroups;
    private ForeignLanguageDTO foreignLanguage;
    //private String[] birthday;
    private GroupDTO group;
    private Long groupIdFromSource;
    private Long foreignLanguageIdFromSource;
}
