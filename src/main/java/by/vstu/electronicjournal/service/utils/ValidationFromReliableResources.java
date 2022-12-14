package by.vstu.electronicjournal.service.utils;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.dto.common.AbstractDTORelatedFromSource;

import java.util.List;

public interface ValidationFromReliableResources<D extends AbstractDTORelatedFromSource> {

    /**
     * For check records in this "cashe" tables. If record is absent in this tables it must be added.
     *
     * @param query to create request to common-info service for search records
     * */
    List<? extends D> validator(String query);
}
