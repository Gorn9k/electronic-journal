package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.SubGroupTypeEntitySubGroupDTO;
import by.vstu.electronicjournal.dto.SubGroupTypeEntitySubGroupStudentDTO;
import by.vstu.electronicjournal.entity.SubGroupTypeEntitySubGroup;
import by.vstu.electronicjournal.entity.SubGroupTypeEntitySubGroupStudent;
import by.vstu.electronicjournal.repository.SubGroupTypeEntitySubGroupRepository;
import by.vstu.electronicjournal.repository.SubGroupTypeEntitySubGroupStudentRepository;
import by.vstu.electronicjournal.service.SubGroupTypeEntitySubGroupService;
import by.vstu.electronicjournal.service.SubGroupTypeEntitySubGroupStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubGroupTypeEntitySubGroupStudentServiceImpl implements SubGroupTypeEntitySubGroupStudentService {

    @Autowired
    private SubGroupTypeEntitySubGroupStudentRepository repository;

    @Override
    public List<SubGroupTypeEntitySubGroupStudent> search(String query) {
        if (query.isEmpty()) {
            return repository.findAll();
        }
        return repository.findAll(getSpecifications(query));
    }

    @Override
    public List<SubGroupTypeEntitySubGroupStudentDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return repository.findAll().stream().map(entity ->
                    mapper.map(entity, SubGroupTypeEntitySubGroupStudentDTO.class)).collect(Collectors.toList());
        }
        return repository.findAll(getSpecifications(query)).stream().map(entity ->
                mapper.map(entity, SubGroupTypeEntitySubGroupStudentDTO.class)).collect(Collectors.toList());
    }

    public SubGroupTypeEntitySubGroupStudent save(SubGroupTypeEntitySubGroupStudent entity) {
        return repository.saveAndFlush(entity);
    }
}
