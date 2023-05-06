package capstone.elsv2.repositories;

import capstone.elsv2.entities.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile,String> {
}
