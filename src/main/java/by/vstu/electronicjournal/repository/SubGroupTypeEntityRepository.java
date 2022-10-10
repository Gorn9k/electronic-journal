package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.JournalSite;
import by.vstu.electronicjournal.entity.SubGroupTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubGroupTypeEntityRepository extends JpaRepository<SubGroupTypeEntity, Long>, JpaSpecificationExecutor<SubGroupTypeEntity> {
}
