package by.vstu.electronicjournal.service.utils;

import by.vstu.electronicjournal.dto.common.AbstractDTO;
import by.vstu.electronicjournal.dto.common.AbstractDTORelatedFromSource;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface ActuatorFromGeneralResources<D extends AbstractDTORelatedFromSource> {

    RestTemplate restTemplate = new RestTemplate();

    List<D> findAndAddThings(String pathToRelatedResources);

}
