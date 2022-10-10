package by.vstu.electronicjournal.service.utils.impl;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.dto.common.AbstractDTORelatedFromSource;
import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.entity.common.AbstractEntityForRelatedFromSource;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.utils.RelationWithParentIdentifiers;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActuatorFromGeneralResourcesImpl<
        E extends AbstractEntityForRelatedFromSource,
        D extends AbstractDTORelatedFromSource,
        R extends RelationWithParentIdentifiers<E> & JpaRepository<E, Long>,
        F extends AbstractFactoryForRelatedResources<E,D>,
        M extends Mapper<E,D>>
        implements ActuatorFromGeneralResources<D> {

    private final Class<D> type;
    private final R repository;
    private final M mapper;
    private final F factory;

    public ActuatorFromGeneralResourcesImpl(Class<D> type, R repository, F factory, M mapper) {
        this.repository = repository;
        this.factory = factory;
        this.mapper = mapper;
        this.type = type;
    }

    @Override
    public List<D> findAndAddThings(String pathToRelatedResources) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<D> listResponseEntity = new ArrayList<>();

        try {
            Objects.requireNonNull(restTemplate.exchange(pathToRelatedResources, HttpMethod.GET, null, new ParameterizedTypeReference<List<?>>() {
            })
                    .getBody()).forEach(entry -> {
                listResponseEntity.add(objectMapper.convertValue(entry, type));
            });
        } catch (RestClientException e) {
            throw new IllegalArgumentException("Access error by address " + pathToRelatedResources);
        }

        List<D> result = searchAndCreate(listResponseEntity);

        if (result.isEmpty()) {
            throw new IllegalArgumentException("Had no records by " + pathToRelatedResources);
        }
        return result;
    }

    private List<D> searchAndCreate(List<? extends D> source) {

        List<D> result = new ArrayList<>();

        for (D dto : source) {
            D tempDTO = null;
            try {
                tempDTO = mapper.toDTO(repository.findByIdFromSource(dto.getId()), type);
            } catch (IllegalArgumentException e) {
                System.err.println("Not found. " + dto + " will be add.");
            }

            if (tempDTO == null) {
                tempDTO = mapper.toDTO(repository.save(factory.create(dto)), type);
            }
            result.add(tempDTO);
        }
        return result;
    }
}
