package capstone.elsv2.repositories;

import capstone.elsv2.entities.SitterPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SitterPackageRepository extends JpaRepository<SitterPackage, String> {
    List<SitterPackage> findAllBy_package_IdAndSitterProfile_Account_Status(String packageId, String status);

    List<SitterPackage> findAllBySitterProfile_Id(String sitterId);

    List<SitterPackage> findAllByStatus(String status);
}
