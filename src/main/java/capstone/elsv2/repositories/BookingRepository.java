package capstone.elsv2.repositories;

import capstone.elsv2.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,String> {
    @Query("SELECT b FROM Booking b where b.status =:status")
    Page<Booking> findAllByStatus(Pageable pageable,String status);
    List<Booking> findAllByStatus (String status);
    List<Booking> findAllByCustomer_Id(String customerId);

    List<Booking> findAllBySitter_Id(String sitterId);

    List<Booking> findAllByStatusAndCustomer_Id(String status,String customerId);
    List<Booking> findAllByStatusOrStatusAndCustomer_Id(String status,String status1,String customerId);
    List<Booking> findAllByStatusOrStatusAndSitter_Id(String status,String status1,String sitterId);

    List<Booking> findAllByStatusAndSitter_Id(String status,String sitterId);

    Page<Booking> findAllByEndDateBefore(Pageable pageable,LocalDate endDate);
    List<Booking> findAllByEndDateBeforeAndCustomer_Id(LocalDate endDate,String customerId);
    List<Booking> findAllByEndDateBeforeAndSitter_Id(LocalDate endDate,String sitterId);

    Integer countAllByStatusOrStatusOrStatusOrStatus(String status, String status1 ,String status2 ,String status3);

    List<Booking> findAllByStatusOrStatusOrStatusOrStatus(String status, String status1 ,String status2 ,String status3);

}
