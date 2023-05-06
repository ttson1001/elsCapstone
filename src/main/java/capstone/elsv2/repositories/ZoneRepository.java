package capstone.elsv2.repositories;

import capstone.elsv2.entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone,String> {
    Zone findByDistrict(String district);
}
