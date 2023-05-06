package capstone.elsv2.repositories;

import capstone.elsv2.entities.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education,String> {
    List<Education> findAllBySitter_IdAndStatus(String id,String status);
}
