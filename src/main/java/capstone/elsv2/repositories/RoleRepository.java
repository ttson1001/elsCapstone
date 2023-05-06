package capstone.elsv2.repositories;

import capstone.elsv2.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    Role findAllByName (String roleName);
}
