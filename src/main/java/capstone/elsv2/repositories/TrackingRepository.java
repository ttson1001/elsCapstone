package capstone.elsv2.repositories;

import capstone.elsv2.entities.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackingRepository extends JpaRepository<Tracking, String> {
    List<Tracking> findAllByBookingDetail_Id(String bookingDetailId);
}
