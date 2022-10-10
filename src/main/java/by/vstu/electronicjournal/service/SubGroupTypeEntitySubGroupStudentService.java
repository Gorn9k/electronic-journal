package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.SubGroupTypeEntitySubGroupDTO;
import by.vstu.electronicjournal.dto.SubGroupTypeEntitySubGroupStudentDTO;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.SubGroupTypeEntitySubGroup;
import by.vstu.electronicjournal.entity.SubGroupTypeEntitySubGroupStudent;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.common.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SubGroupTypeEntitySubGroupStudentService {

    ModelMapper mapper = new ModelMapper();

    default Specification<SubGroupTypeEntitySubGroupStudent> getSpecifications(String query) {
        try {
            Node rootNode = new RSQLParser().parse(query);

            return rootNode.accept(new CustomRsqlVisitor<>());

        } catch (Exception e) {
            throw new IllegalArgumentException("There are errors in the search query string");
        }
    }

    List<SubGroupTypeEntitySubGroupStudent> search(String query);

    List<SubGroupTypeEntitySubGroupStudentDTO> searchAndMapInDTO(String query);

    SubGroupTypeEntitySubGroupStudent save(SubGroupTypeEntitySubGroupStudent entity);
}
