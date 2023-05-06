package capstone.elsv2.repositories;

import capstone.elsv2.entities.WorkingTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkingTimeRepository extends JpaRepository<WorkingTime,String> {

    List<WorkingTime> findAllBySitter_Id(String sitterId);

    List<WorkingTime> findAllBySitter_IdAndAndStatus(String sitterId,String status);
}
