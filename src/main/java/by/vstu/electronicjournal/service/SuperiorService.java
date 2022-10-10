package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.SuperiorDTO;
import by.vstu.electronicjournal.entity.Superior;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;

import java.util.List;

public interface SuperiorService extends CRUDService<SuperiorDTO>, RSQLSearch<Superior, SuperiorDTO> {
}
