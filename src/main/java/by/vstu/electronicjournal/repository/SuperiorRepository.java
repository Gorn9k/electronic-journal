package by.vstu.electronicjournal.repository;

import by.vstu.electronicjournal.entity.Superior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SuperiorRepository extends JpaRepository<Superior, Long>,
        JpaSpecificationExecutor<Superior>  {

}
