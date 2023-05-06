package capstone.elsv2.repositories;

import capstone.elsv2.entities.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion,String> {
    @Query("SELECT p FROM Promotion p WHERE " +
            "p.name LIKE %:keyWord% " +
            "or p.code LIKE %:keyWord% " +
            "or p.status LIKE %:keyWord% " +
            "or p.description LIKE  %:keyWord% " )
    Page<Promotion> findAllByKeyWord(Pageable pageable, String keyWord);

    List<Promotion> findAllByStatus(String status);

}
