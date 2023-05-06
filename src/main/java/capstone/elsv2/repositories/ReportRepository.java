package capstone.elsv2.repositories;

import capstone.elsv2.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, String> {

    List<Report> findAllByStatus(String status);

    List<Report> findAllBySitter_IdAndStatus(String sitterId, String status);

    List<Report> findAllByCustomer_IdAndStatus(String customerId, String status);

    List<Report> findAllByCustomer_IdOrderByCreateDateDesc(String customerId);

    List<Report> findAllBySitter_IdOrderByCreateDateDesc(String sitterId);

    List<Report> findAllByBookingDetail_Id(String bookingDetailId);
}
