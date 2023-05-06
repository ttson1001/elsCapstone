package capstone.elsv2.repositories;

import capstone.elsv2.entities.Elder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElderRepository extends JpaRepository<Elder,String> {
    Elder findByIdCardNumber(String cardId);

    List<Elder> findAllByStatus(String status);

    Integer countAllByStatus(String status);

}
