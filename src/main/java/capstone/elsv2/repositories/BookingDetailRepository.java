package capstone.elsv2.repositories;

import capstone.elsv2.entities.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingDetailRepository extends JpaRepository<BookingDetail, String> {
    List<BookingDetail> findAllByBooking_Id(String bookingId);

    List<BookingDetail> findAllBy_package_Id(String packageId);
    @Query("SELECT bd FROM BookingDetail bd WHERE MONTH(bd.startDateTime)= :month and DAY(bd.startDateTime)= :day and YEAR(bd.startDateTime) = :year ")
    List<BookingDetail> findAllByStartDateTime(int month, int day, int year);
    List<BookingDetail> findAllByBooking_IdAndStatus(String bookingId, String status);
    @Query("SELECT bd FROM BookingDetail bd WHERE MONTH(bd.startDateTime)= :month and DAY(bd.startDateTime)= :day and YEAR(bd.startDateTime) = :year and bd.booking.id = :id")
    BookingDetail findDetail(String id, int day, int month, int year);


    @Query(value ="SELECT * FROM booking_detail bd " +
            "left join booking b on b.id = bd.booking_id " +
            "where b.elder_id like ?1 " +
            "and (bd.status like 'WAITING' " +
            "or bd.status like 'WORKING') " +
            "and bd.start_date_time >= ?2",
    nativeQuery = true)
    List<BookingDetail> getListBookingDetailToValidDateTime(String elderID, LocalDate dateStart);
}
