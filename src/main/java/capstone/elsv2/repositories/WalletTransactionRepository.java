package capstone.elsv2.repositories;

import capstone.elsv2.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<Transaction, String> {
    @Query("SELECT w FROM Transaction w WHERE YEAR(w.createDateTime) = :year and w.type = :type")
    List<Transaction> findAllByYear(Integer year, String type);
    @Query("SELECT w FROM Transaction w WHERE YEAR(w.createDateTime) = :year and w.type = :type and w.id = :id")
    List<Transaction> findAllByYearForSitter(Integer year, String type, String id);

    @Query("SELECT w FROM Transaction w WHERE MONTH(w.createDateTime)= :month and YEAR(w.createDateTime) = :year and w.type = :type")
    List<Transaction> findAllByMonth(Integer month, Integer year, String type);
    @Query("SELECT w FROM Transaction w WHERE MONTH(w.createDateTime)= :month and YEAR(w.createDateTime) = :year and w.type = :type and w.id = :id")
    List<Transaction> findAllByMonthForSitter(Integer month, Integer year, String type, String id);
    @Query("SELECT w FROM Transaction w WHERE MONTH(w.createDateTime)= :month and DAY(w.createDateTime)= :day and YEAR(w.createDateTime) = :year and w.type = :type")
    List<Transaction> findAllByDate(Integer day, Integer month, Integer year, String type);
    @Query("SELECT w FROM Transaction w WHERE MONTH(w.createDateTime)= :month and DAY(w.createDateTime)= :day and YEAR(w.createDateTime) = :year and w.type = :type and w.id = :id")
    List<Transaction> findAllByDateForSitter(Integer day, Integer month, Integer year, String type, String id);

    List<Transaction> findAllByWallet_Id(String adminId);

    List<Transaction> findAllByWallet_IdOrderByCreateDateTimeDesc(String userId);
}
