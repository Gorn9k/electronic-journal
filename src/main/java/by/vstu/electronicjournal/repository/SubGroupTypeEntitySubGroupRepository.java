package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.SubGroupTypeEntitySubGroup;
import by.vstu.electronicjournal.entity.SubGroupTypeEntitySubGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubGroupTypeEntitySubGroupRepository extends JpaRepository<SubGroupTypeEntitySubGroup, SubGroupTypeEntitySubGroupId>,
        JpaSpecificationExecutor<SubGroupTypeEntitySubGroup> {
}
