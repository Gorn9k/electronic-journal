package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.JournalContentDTO;
import by.vstu.electronicjournal.dto.JournalSiteDTO;
import by.vstu.electronicjournal.dto.StudentDTO;
import by.vstu.electronicjournal.dto.StudentPerformanceDTO;
import by.vstu.electronicjournal.entity.*;
import by.vstu.electronicjournal.entity.common.Status;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.JournalContentRepository;
import by.vstu.electronicjournal.repository.JournalSiteRepository;
import by.vstu.electronicjournal.repository.StudentRepository;
import by.vstu.electronicjournal.service.JournalContentService;
import by.vstu.electronicjournal.service.StudentService;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JournalContentServiceImpl
        extends CommonCRUDServiceImpl<JournalContent, JournalContentDTO, JournalContentRepository>
        implements JournalContentService {

    @Autowired
    private Mapper<JournalContent, JournalContentDTO> mapper;

    @Autowired
    private JournalContentRepository journalContentRepository;

    @Autowired
    private StudentService studentService;

    public JournalContentServiceImpl() {
        super(JournalContent.class, JournalContentDTO.class);
    }

    @Override
    public List<JournalContent> search(String query) {
        if (query.isEmpty()) {
            return journalContentRepository.findAll();
        }
        return journalContentRepository.findAll(getSpecifications(query));
    }

    @Override
    public List<JournalContentDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(journalContentRepository.findAll(getSpecifications(query)), JournalContentDTO.class);
    }

    @Override
    public void generate(List<JournalSite> params) {

        for (JournalSite journalSite : params) {
            for (JournalHeader journalHeader : journalSite.getJournalHeaders()) {
                generate(journalHeader);
            }
        }
    }

    @Override
    public void generate(JournalHeader header) {
        if (header.getJournalContents() == null || header.getJournalContents().size() == 0) {
            //String query = String.format("group.name==%s;subGroupIdentificator=", header.getJournalSite().getGroup().getName());
            String query = String.format("group.name==%s", header.getJournalSite().getGroup().getName());
            /*
            if (header.getSubGroup() == 0) {
                query += "in=(1,2)";
            } else {
                query += "=" + header.getSubGroup();
            }
             */
            //List<StudentDTO> studentDTOS = (List<StudentDTO>) studentService.validator(query);
            List<Student> students = studentService.search(query);
            if (header.getSubGroup() != 0) {
                students = students.stream().filter(student -> student.getSubGroupTypeEntitySubGroupStudents().stream().anyMatch(subGroupTypeEntitySubGroupStudent ->
                        subGroupTypeEntitySubGroupStudent.getSubGroupTypeEntitySubGroup().getSubGroup().getSubGroupNumber().equals(header.getSubGroup()) &&
                                subGroupTypeEntitySubGroupStudent.getSubGroupTypeEntitySubGroup().getSubGroupTypeEntity().getSubGroupType().equals(SubGroupType.BY_GROUP))).collect(Collectors.toList());
            }
            for (Student student : students) {
                JournalContent journalContent = new JournalContent();
                // student.setGroup(header.getJournalSite().getGroup());
                journalContent.setStatus(Status.ACTIVE);
                journalContent.setJournalHeader(header);
                journalContent.setStudent(student);
                journalContentRepository.save(journalContent);
            }
        }
    }
}
