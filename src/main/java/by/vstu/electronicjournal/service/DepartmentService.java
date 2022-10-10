package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.DepartmentDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Department;
import by.vstu.electronicjournal.repository.DepartmentRepository;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.DepartmentFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface DepartmentService extends CRUDService<DepartmentDTO>, RSQLSearch<Department, DepartmentDTO> {

    List<Department> updateFromDekanat(List<DepartmentDTO> dto);

    List<DepartmentDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress);
}
