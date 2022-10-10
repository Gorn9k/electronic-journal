package by.vstu.electronicjournal.repository.utils;

import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.entity.common.AbstractEntityForRelatedFromSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RelationWithParentIdentifiers<E extends AbstractEntityForRelatedFromSource> {

    E findByIdFromSource(Long idFromSource);
}
