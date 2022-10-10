package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Discipline;
import by.vstu.electronicjournal.repository.utils.RelationWithParentIdentifiers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AcademicTitleRepository extends RelationWithParentIdentifiers<AcademicTitle>, JpaRepository<AcademicTitle, Long>,
        JpaSpecificationExecutor<AcademicTitle> {
}
