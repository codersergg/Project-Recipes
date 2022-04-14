package recipes.com.codersergg.security;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.com.codersergg.repository.CustomerRepository;

@Service
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Boolean isCustomerExists = customerRepository.existsCustomerByEmailIgnoreCase(email);

        if (!isCustomerExists) {
            throw new UsernameNotFoundException("Not found: " + email);
        }

        return new UserDetailsImpl(customerRepository.findCustomerByEmailIgnoreCase(email));
    }
}