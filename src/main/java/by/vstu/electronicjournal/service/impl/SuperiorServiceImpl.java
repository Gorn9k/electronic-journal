package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.SuperiorDTO;
import by.vstu.electronicjournal.dto.TeacherDTO;
import by.vstu.electronicjournal.entity.Superior;
import by.vstu.electronicjournal.entity.Teacher;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.SuperiorRepository;
import by.vstu.electronicjournal.repository.TeacherRepository;
import by.vstu.electronicjournal.service.SuperiorService;
import by.vstu.electronicjournal.service.TeacherService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.factory.TeacherFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class SuperiorServiceImpl extends CommonCRUDServiceImpl<Superior, SuperiorDTO, SuperiorRepository>
        implements SuperiorService {

    public SuperiorServiceImpl() {
        super(Superior.class, SuperiorDTO.class);
    }

    @Autowired
    private Mapper<Superior, SuperiorDTO> mapper;

    @Autowired
    private SuperiorRepository superiorRepository;

    @Override
    public List<Superior> search(String query) {
        if (query.isEmpty()) {
            return superiorRepository.findAll();
        }
        return superiorRepository.findAll(getSpecifications(query));
    }

    @Override
    public List<SuperiorDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(superiorRepository.findAll(getSpecifications(query)), SuperiorDTO.class);
    }

}
