package by.vstu.electronicjournal.service.common;

import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.service.common.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.jpa.domain.Specification;

import javax.swing.text.AbstractDocument;
import java.util.List;

/**
 * Interface for using RSQL searching
 *
 * @param <E> Class entity for searching. Must be extends from {@link AbstractEntity}
 */
public interface RSQLSearch<E extends AbstractEntity, D extends AbstractDTO> {

    /**
     * Create {@link Specification} for search.
     *
     * @param query data for create specifications
     * @return Specification for selected entity
     * @throws IllegalArgumentException if in query was errors
     */
    default Specification<E> getSpecifications(String query) {
        try {
            Node rootNode = new RSQLParser().parse(query);

            return (Specification<E>) rootNode.accept(new CustomRsqlVisitor<E>());

        } catch (Exception e) {
            throw new IllegalArgumentException("There are errors in the search query string");
        }
    }

    List<E> search(String query);

    List<D> searchAndMapInDTO(String query);
}
