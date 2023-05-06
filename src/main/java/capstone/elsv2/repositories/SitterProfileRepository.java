package capstone.elsv2.repositories;

import capstone.elsv2.entities.SitterProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SitterProfileRepository extends JpaRepository<SitterProfile,String> {
    Page<SitterProfile> findAllByAccount_StatusOrAccount_Status(Pageable pageable, String status, String status1 );

    Page<SitterProfile> findAllByAccount_Status(Pageable pageable,String status);
    List<SitterProfile> findAllByAccount_Status(String status);

    @Query(value = "SELECT DISTINCT * FROM sitter_profile s\n" +
            "            LEFT JOIN zone z ON z.zone_id = s.zone_zone_id\n" +
            "            LEFT JOIN account a ON a.id = s.account_id\n" +
            "            JOIN sitter_package spk ON spk.sitter_id = a.id\n" +
            "            JOIN package p ON spk.package_id = p.id\n" +
            "            WHERE\n" +
            "            p.id LIKE ?1\n" +
            "            AND a.status LIKE 'ACTIVATE'\n" +
            "            AND spk.status LIKE 'ACTIVATE'\n" +
            "            AND z.district LIKE ?2",nativeQuery = true)
    List<SitterProfile> getActivateSitterByPackageAndZone(String packageId,String zoneId);
}
