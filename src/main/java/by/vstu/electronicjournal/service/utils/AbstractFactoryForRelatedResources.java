package by.vstu.electronicjournal.service.utils;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.dto.common.AbstractDTORelatedFromSource;
import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.entity.common.AbstractEntityForRelatedFromSource;

public interface AbstractFactoryForRelatedResources<E extends AbstractEntityForRelatedFromSource, D extends AbstractDTORelatedFromSource> {

    E create(D dto);

    E update(E entity, D dto);
}
