package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.SubGroupTypeEntitySubGroupStudent;
import by.vstu.electronicjournal.entity.SubGroupTypeEntitySubGroupStudentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubGroupTypeEntitySubGroupStudentRepository extends JpaRepository<SubGroupTypeEntitySubGroupStudent, SubGroupTypeEntitySubGroupStudentId>,
        JpaSpecificationExecutor<SubGroupTypeEntitySubGroupStudent> {
}
