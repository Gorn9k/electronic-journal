package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.SubGroupTypeEntityDTO;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.SubGroupTypeEntity;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.JournalSiteRepository;
import by.vstu.electronicjournal.repository.SubGroupTypeEntityRepository;
import by.vstu.electronicjournal.service.SubGroupTypeEntityService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubGroupTypeEntityServiceImpl extends CommonCRUDServiceImpl<SubGroupTypeEntity, SubGroupTypeEntityDTO, SubGroupTypeEntityRepository>
        implements SubGroupTypeEntityService {

    @Autowired
    private SubGroupTypeEntityRepository repository;
    @Autowired
    private Mapper<SubGroupTypeEntity, SubGroupTypeEntityDTO> mapper;

    public SubGroupTypeEntityServiceImpl() {
        super(SubGroupTypeEntity.class, SubGroupTypeEntityDTO.class);
    }

    @Override
    public List<SubGroupTypeEntity> search(String query) {
        if (query.isEmpty()) {
            return repository.findAll();
        }
        return repository.findAll(getSpecifications(query));
    }

    @Override
    public List<SubGroupTypeEntityDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(repository.findAll(getSpecifications(query)), SubGroupTypeEntityDTO.class);
    }
}
