package by.vstu.electronicjournal.service.impl;

import by.vstu.electronicjournal.dto.StudentDTO;
import by.vstu.electronicjournal.entity.*;
import by.vstu.electronicjournal.mapper.Mapper;
import by.vstu.electronicjournal.repository.*;
import by.vstu.electronicjournal.service.*;
import by.vstu.electronicjournal.service.common.impl.CommonCRUDServiceImpl;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;
import by.vstu.electronicjournal.service.utils.ActuatorFromGeneralResources;
import by.vstu.electronicjournal.service.utils.UpdateFromRelatedService;
import by.vstu.electronicjournal.service.utils.factory.StudentFactory;
import by.vstu.electronicjournal.service.utils.impl.ActuatorFromGeneralResourcesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl
        extends CommonCRUDServiceImpl<Student, StudentDTO, StudentRepository>
        implements StudentService, UpdateFromRelatedService<Student, StudentDTO, StudentRepository, StudentFactory> {

    @Value("${dekanat}")
    private String path;

    @Autowired
    private Mapper<Student, StudentDTO> mapper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ForeignLanguageRepository foreignLanguageRepository;

    private ActuatorFromGeneralResources<StudentDTO> relatedResources;

    @Autowired
    private JournalSiteService journalSiteService;

    @Autowired
    private SubGroupService subGroupService;

    @Autowired
    private SubGroupTypeEntityService subGroupTypeEntityService;

    @Autowired
    private SubGroupTypeEntitySubGroupService subGroupTypeEntitySubGroupService;

    @Autowired
    private SubGroupTypeEntitySubGroupStudentService subGroupTypeEntitySubGroupStudentService;

    public StudentServiceImpl() {
        super(Student.class, StudentDTO.class);
    }

    private AbstractFactoryForRelatedResources<Student, StudentDTO> studentFactory;

    @PostConstruct
    void settingUp() {
        studentFactory = new StudentFactory(foreignLanguageRepository, groupRepository);
        relatedResources = new ActuatorFromGeneralResourcesImpl(StudentDTO.class, studentRepository, studentFactory, mapper);
    }

    @Override
    public List<Student> search(String query) {
        if (query.isEmpty()) {
            return studentRepository.findAll();
        }
        return studentRepository.findAll(getSpecifications(query));
    }

    @Override
    public List<StudentDTO> searchAndMapInDTO(String query) {
        if (query.isEmpty()) {
            return findAll();
        }
        return mapper.toDTOs(studentRepository.findAll(getSpecifications(query)), StudentDTO.class);
    }

    @Override
    public List<StudentDTO> getStudentsByGroup(String query) {
        List<JournalSite> journalSites = journalSiteService.search(query);
        List<Student> students = new ArrayList<>();
        if (journalSites.size()!=0) {
            journalSites.get(0).getJournalHeaders().get(0).getJournalContents().stream().forEach(journalContent ->
                    students.add(journalContent.getStudent()));
        }
        return mapper.toDTOs(students, StudentDTO.class);
    }

/*
    @Override
    public List<StudentDTO> validator(String query) {
        String queryToCommonInfo = String.format("%s/students/search?q=%s", path, query);
        return relatedResources.findAndAddThings(queryToCommonInfo);
    }

 */

    @Override
    @Transactional
    public void addGeneralSubGroupOnStudents() {
        groupRepository.findAll().forEach(group -> setGeneralSubGroupOnStudents(group.getStudents()));
    }

    private void setGeneralSubGroupOnStudents(List<Student> students) {
        Set<Student> studentSet = new TreeSet<>(students);
        int studentSize = studentSet.size();
        int maxCount = 15;
        Double halfCountOnGroup = Math.ceil(studentSize/2.0);
        AtomicReference<Integer> currentCount = new AtomicReference<>(1);
        studentSet.forEach(student -> {
            SubGroupTypeEntitySubGroup subGroupTypeEntitySubGroup = null;
            if (studentSize <= maxCount || currentCount.get() <= halfCountOnGroup) {
                List<SubGroupTypeEntitySubGroup> list = subGroupTypeEntitySubGroupService.search("subGroup.subGroupNumber==1").stream().filter(subGroupTypeEntitySubGroup1 ->
                        subGroupTypeEntitySubGroup1.getSubGroupTypeEntity().getSubGroupType().getSubGroupType().equals("BY_GROUP")).collect(Collectors.toList());
                subGroupTypeEntitySubGroup = list.isEmpty() ? null : list.get(0);
                if (subGroupTypeEntitySubGroup == null) {
                    subGroupTypeEntitySubGroup = new SubGroupTypeEntitySubGroup();
                    subGroupTypeEntitySubGroup.setSubGroup(subGroupService.search("subGroupNumber==1").get(0));
                    subGroupTypeEntitySubGroup.setSubGroupTypeEntity(subGroupTypeEntityService.search("").stream().filter(subGroupTypeEntity ->
                            subGroupTypeEntity.getSubGroupType().getSubGroupType().equals("BY_GROUP")).collect(Collectors.toList()).get(0));
                    //subGroupTypeEntitySubGroup = subGroupTypeEntitySubGroupService.save(subGroupTypeEntitySubGroup);
                }
                if (currentCount.get() <= halfCountOnGroup) {
                    currentCount.getAndSet(currentCount.get() + 1);
                }
            } else if (currentCount.get() > halfCountOnGroup) {
                List<SubGroupTypeEntitySubGroup> list = subGroupTypeEntitySubGroupService.search("subGroup.subGroupNumber==2").stream().filter(subGroupTypeEntitySubGroup1 ->
                        subGroupTypeEntitySubGroup1.getSubGroupTypeEntity().getSubGroupType().getSubGroupType().equals("BY_GROUP")).collect(Collectors.toList());
                subGroupTypeEntitySubGroup = list.isEmpty() ? null : list.get(0);
                if (subGroupTypeEntitySubGroup == null) {
                    subGroupTypeEntitySubGroup = new SubGroupTypeEntitySubGroup();
                    subGroupTypeEntitySubGroup.setSubGroup(subGroupService.search("subGroupNumber==2").get(0));
                    subGroupTypeEntitySubGroup.setSubGroupTypeEntity(subGroupTypeEntityService.search("").stream().filter(subGroupTypeEntity ->
                            subGroupTypeEntity.getSubGroupType().getSubGroupType().equals("BY_GROUP")).collect(Collectors.toList()).get(0));
                    //subGroupTypeEntitySubGroup = subGroupTypeEntitySubGroupService.save(subGroupTypeEntitySubGroup);
                }
                currentCount.getAndSet(currentCount.get() + 1);
            }
            List<SubGroupTypeEntitySubGroupStudent> subGroupTypeEntitySubGroupStudents = new ArrayList<>();
            SubGroupTypeEntitySubGroupStudent subGroupTypeEntitySubGroupStudent = new SubGroupTypeEntitySubGroupStudent();
            subGroupTypeEntitySubGroupStudent.setStudent(student);
            subGroupTypeEntitySubGroupStudent.setSubGroupTypeEntitySubGroup(subGroupTypeEntitySubGroup);
            subGroupTypeEntitySubGroupStudents.add(subGroupTypeEntitySubGroupStudent);
            List<SubGroupTypeEntitySubGroup> list = subGroupTypeEntitySubGroupService.search("subGroup.subGroupNumber==0").stream().filter(subGroupTypeEntitySubGroup1 ->
                    subGroupTypeEntitySubGroup1.getSubGroupTypeEntity().getSubGroupType().getSubGroupType().equals("BY_FOREIGN_LANGUAGE")).collect(Collectors.toList());
            SubGroupTypeEntitySubGroup subGroupTypeEntitySubGroup2 = list.isEmpty() ? null : list.get(0);
            if (subGroupTypeEntitySubGroup2 == null) {
                subGroupTypeEntitySubGroup2 = new SubGroupTypeEntitySubGroup();
                subGroupTypeEntitySubGroup2.setSubGroup(subGroupService.search("subGroupNumber==0").get(0));
                subGroupTypeEntitySubGroup2.setSubGroupTypeEntity(subGroupTypeEntityService.search("").stream().filter(subGroupTypeEntity ->
                        subGroupTypeEntity.getSubGroupType().getSubGroupType().equals("BY_FOREIGN_LANGUAGE")).collect(Collectors.toList()).get(0));
                //subGroupTypeEntitySubGroup = subGroupTypeEntitySubGroupService.save(subGroupTypeEntitySubGroup);
            }
            subGroupTypeEntitySubGroupStudent = new SubGroupTypeEntitySubGroupStudent();
            subGroupTypeEntitySubGroupStudent.setStudent(student);
            subGroupTypeEntitySubGroupStudent.setSubGroupTypeEntitySubGroup(subGroupTypeEntitySubGroup2);
            subGroupTypeEntitySubGroupStudents.add(subGroupTypeEntitySubGroupStudent);
            student.setSubGroupTypeEntitySubGroupStudents(subGroupTypeEntitySubGroupStudents);
            //System.out.println(student);
            //studentRepository.saveAndFlush(student);
        });
    }

    public List<Student> updateFromDekanat(List<StudentDTO> dto) {
        return defaultUpdateFromRelatedService(dto, studentRepository, (StudentFactory) studentFactory);
    }

    public List<StudentDTO> getFromDekanat(RestTemplate restTemplate, String serviceAddress) {
        return defaultGetFromRelatedService(restTemplate, serviceAddress + "/students?query=", StudentDTO.class);
    }
}
