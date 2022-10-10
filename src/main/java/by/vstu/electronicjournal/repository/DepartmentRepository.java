package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Department;
import by.vstu.electronicjournal.repository.utils.RelationWithParentIdentifiers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DepartmentRepository extends RelationWithParentIdentifiers<Department>, JpaRepository<Department, Long>,
        JpaSpecificationExecutor<Department> {
}
