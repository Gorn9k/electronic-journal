package by.vstu.electronicjournal.service;

import by.vstu.electronicjournal.dto.AcademicPerformanceDTO;
import by.vstu.electronicjournal.dto.JournalContentDTO;
import by.vstu.electronicjournal.dto.JournalHeaderDTO;
import by.vstu.electronicjournal.dto.requestBodyParams.ParamsForCreateJournalHeader;
import by.vstu.electronicjournal.dto.requestBodyParams.PatternDTO;
import by.vstu.electronicjournal.entity.JournalHeader;
import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.service.common.CRUDService;
import by.vstu.electronicjournal.service.common.RSQLSearch;
import java.util.List;

public interface JournalHeaderService extends CRUDService<JournalHeaderDTO>,
	RSQLSearch<JournalHeader, JournalHeaderDTO> {

	/**
	 * Create new JournalHeader
	 */
	JournalHeaderDTO create(ParamsForCreateJournalHeader params);

	/**
	 * Generate journal for employees. This method must be run after generate journalSite. Right
	 * now, this method start if it was called. In future, it must be run automatically
	 *
	 * @deprecated
	 */
	List<JournalSite> generate(List<JournalSite> params);

	List<JournalSite> generate(List<JournalSite> params, List<PatternDTO> patternDTOS);

	List<JournalContentDTO> editList(Long id, List<JournalContentDTO> dtos);

	AcademicPerformanceDTO getTotalNumberMissedClassesByStudentForPeriod(String query);
}
