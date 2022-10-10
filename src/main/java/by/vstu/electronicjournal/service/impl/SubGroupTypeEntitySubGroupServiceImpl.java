package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.SubGroupTypeEntityDTO;
import by.vstu.electronicjournal.dto.SubGroupTypeEntitySubGroupDTO;
import by.vstu.electronicjournal.entity.SubGroupTypeEntity;
import by.vstu.electronicjournal.entity.SubGroupTypeEntitySubGroup;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.SubGroupTypeEntityRepository;
import by.vstu.electronicjournal.repository.SubGroupTypeEntitySubGroupRepository;
import by.vstu.electronicjournal.service.SubGroupTypeEntitySubGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubGroupTypeEntitySubGroupServiceImpl implements SubGroupTypeEntitySubGroupService {

    @Autowired
    private SubGroupTypeEntitySubGroupRepository repository;

    @Override
    public List<SubGroupTypeEntitySubGroup> search(String query) {
        if (query.isEmpty()) {
            return repository.findAll();
        }
        return repository.findAll(getSpecifications(query));
    }

    @Override
    public List<SubGroupTypeEntitySubGroupDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return repository.findAll().stream().map(entity ->
                    mapper.map(entity, SubGroupTypeEntitySubGroupDTO.class)).collect(Collectors.toList());
        }
        return repository.findAll(getSpecifications(query)).stream().map(entity ->
                mapper.map(entity, SubGroupTypeEntitySubGroupDTO.class)).collect(Collectors.toList());
    }

    public SubGroupTypeEntitySubGroup save(SubGroupTypeEntitySubGroup entity) {
        return repository.saveAndFlush(entity);
    }
}
