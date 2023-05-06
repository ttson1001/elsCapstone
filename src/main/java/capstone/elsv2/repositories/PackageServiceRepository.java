package capstone.elsv2.repositories;

import capstone.elsv2.entities.PackageService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageServiceRepository extends JpaRepository<PackageService,String> {
    PackageService findPackageServiceBy_package_IdAndService_Id(String packageId, String serviceId);

    List<PackageService> findAllBy_package_Id(String id);

    List<PackageService> findAllByService_Id(String id);

}
