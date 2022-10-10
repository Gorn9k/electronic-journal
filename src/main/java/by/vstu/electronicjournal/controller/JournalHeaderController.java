package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.AcademicPerformanceDTO;
import by.vstu.electronicjournal.dto.JournalContentDTO;
import by.vstu.electronicjournal.dto.JournalHeaderDTO;
import by.vstu.electronicjournal.dto.requestBodyParams.ParamsForCreateJournalHeader;
import by.vstu.electronicjournal.service.JournalHeaderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("journal-headers")
public class JournalHeaderController {

	@Autowired
	private JournalHeaderService JournalHeaderService;

	@Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("search")
	public List<JournalHeaderDTO> search(@RequestParam("q") String query) {
		return JournalHeaderService.searchAndMapInDTO(query);
	}

	@Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("getAcademicPerformance")
	public AcademicPerformanceDTO getTotalNumberMissedClassesByStudentForPeriod(@RequestParam("q") String query) {
		return JournalHeaderService.getTotalNumberMissedClassesByStudentForPeriod(query);
	}

	@Secured({"ROLE_ADMIN"})
	@PostMapping
	public JournalHeaderDTO create(@RequestBody ParamsForCreateJournalHeader dto) {
		return JournalHeaderService.create(dto);
	}

	@Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("{id}")
	public JournalHeaderDTO getById(@PathVariable("id") Long id) {
		return JournalHeaderService.findOne(id);
	}

	@Secured({"ROLE_ADMIN"})
	@PatchMapping("{id}")
	public JournalHeaderDTO editById(@PathVariable("id") Long id,
		@RequestBody JournalHeaderDTO dto) {
		return JournalHeaderService.update(id, dto);
	}

	@Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
	@PatchMapping("{id}/content")
	public List<JournalContentDTO> editList(@PathVariable("id") Long id,
		@RequestBody List<JournalContentDTO> dtos) {
		return JournalHeaderService.editList(id, dtos);
	}

	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("{id}")
	public void deleteById(@PathVariable("id") Long id) {
		JournalHeaderService.deleteById(id);
	}
}
