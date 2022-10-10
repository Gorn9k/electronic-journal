package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.TypeClassDTO;
import by.vstu.electronicjournal.entity.TypeClass;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.ValidationFromReliableResources;

import java.util.List;

public interface TypeClassService
        extends CRUDService<TypeClassDTO>, RSQLSearch<TypeClass, TypeClassDTO>, ValidationFromReliableResources<TypeClassDTO> {
}
