package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Faculty;
import by.vstu.electronicjournal.repository.utils.RelationWithParentIdentifiers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FacultyRepository extends RelationWithParentIdentifiers<Faculty>, JpaRepository<Faculty, Long>,
        JpaSpecificationExecutor<Faculty> {
}
