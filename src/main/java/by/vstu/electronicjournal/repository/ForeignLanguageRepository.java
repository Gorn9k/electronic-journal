package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.ForeignLanguage;
import by.vstu.electronicjournal.entity.JournalContent;
import by.vstu.electronicjournal.repository.utils.RelationWithParentIdentifiers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ForeignLanguageRepository extends RelationWithParentIdentifiers<ForeignLanguage>, JpaRepository<ForeignLanguage, Long>,
        JpaSpecificationExecutor<ForeignLanguage> {
}
