package capstone.elsv2.repositories;

import capstone.elsv2.entities.Elder;
import capstone.elsv2.entities.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationshipRepository extends JpaRepository<Relationship,String> {
    List<Relationship> findByCustomer_Id(String id);
    List<Relationship> findAllByElder_Id(String id);
}
