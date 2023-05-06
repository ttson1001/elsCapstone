package capstone.elsv2.repositories;

import capstone.elsv2.entities.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkExpRepository extends JpaRepository<WorkExperience,String> {
    List<WorkExperience> findAllBySitter_IdAndStatus(String id,String status);
}
