package capstone.elsv2.authentication;

import capstone.elsv2.entities.Account;
import capstone.elsv2.repositories.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {
    private final AccountRepository accountRepository;

    public MyUserService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username);
        if(account == null){
            throw new UsernameNotFoundException("USER_NOT_FOUND");
        }
        return new MyUserDetail(account);
    }
}
