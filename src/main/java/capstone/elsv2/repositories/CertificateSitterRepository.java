package capstone.elsv2.repositories;

import capstone.elsv2.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateSitterRepository extends JpaRepository<Certificate,String> {
    List<Certificate> findAllBySitterProfile_Account_IdAndStatus(String sitterId, String status);
}
