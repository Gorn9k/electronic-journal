package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.SubGroupTypeEntityDTO;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.SubGroupTypeEntity;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;

public interface SubGroupTypeEntityService extends CRUDService<SubGroupTypeEntityDTO>, RSQLSearch<SubGroupTypeEntity, SubGroupTypeEntityDTO> {

}
