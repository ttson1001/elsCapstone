package capstone.elsv2.repositories;

import capstone.elsv2.entities.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository  extends JpaRepository<Package,String> {
    List<Package> findAllByStatus(String status);
    List<Package> findAllByHealthStatusAndStatus(String healthStatus, String status);
    List<Package> findAllByStatusAndNameContains(String status, String keyWord);
}
