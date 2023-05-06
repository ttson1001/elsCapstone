package capstone.elsv2.repositories;

import capstone.elsv2.entities.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<FeedBack,String> {
    List<FeedBack> findAllBySitter_Id(String sitterId);
}
