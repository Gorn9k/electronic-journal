package by.vstu.electronicjournal.service.utils;

import by.vstu.electronicjournal.dto.common.AbstractDTORelatedFromSource;
import by.vstu.electronicjournal.entity.common.AbstractEntity;
import by.vstu.electronicjournal.entity.common.AbstractEntityForRelatedFromSource;
import by.vstu.electronicjournal.repository.utils.RelationWithParentIdentifiers;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface UpdateFromRelatedService<E extends AbstractEntityForRelatedFromSource, D extends AbstractDTORelatedFromSource,
        R extends RelationWithParentIdentifiers<E> & JpaRepository<E, Long>, F extends AbstractFactoryForRelatedResources<E, D>> {

    default List<D> defaultGetFromRelatedService(RestTemplate restTemplate, String query, Class<D> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return Objects.requireNonNull(restTemplate.exchange(query, HttpMethod.GET, null, new ParameterizedTypeReference<List<?>>() {
        }).getBody()).stream().map(d -> objectMapper.convertValue(d, clazz)).collect(Collectors.toList());
    }

    default List<E> defaultUpdateFromRelatedService(List<D> dtos, R repository, F factory) {
        List<E> updatable = new ArrayList<>();
        dtos.forEach(dto -> {
            E entity = repository.findByIdFromSource(dto.getIdFromSource());
            if (entity == null) {
                entity = factory.create(dto);
            } else {
                entity = factory.update(entity, dto);
            }
            updatable.add(entity);
        });
        return repository.saveAll(updatable);
    }
}
