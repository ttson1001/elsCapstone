package capstone.elsv2.repositories;

import capstone.elsv2.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,String> {
    Account findByEmail(String email);
    Account findByPhone(String phone);
    Page<Account> findAllByRole_NameAndStatusAndStatus(Pageable pageable,String role,String status1, String status2);
    Integer countAccountByRole_Name(String roleName);
    Page<Account> findAllByRole_NameAndStatus(Pageable pageable, String role,String status);
    Page<Account> findAllByRole_NameAndStatusOrStatus(Pageable pageable, String role,String status, String _status);
    Account findByRole_Name(String role);
    Page<Account> findAllByRole_Name(Pageable pageable,String role);

    @Query("SELECT a from Account a join Role r on a.role.id = r.id where r.name=:role ")
    List<Account> findAllByEO(String role);



}
