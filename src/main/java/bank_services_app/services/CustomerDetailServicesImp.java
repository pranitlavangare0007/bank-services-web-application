package bank_services_app.services;

import bank_services_app.repositry.CustomerRepo;
import bank_services_app.util.Role;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailServicesImp implements UserDetailsService {


    private final CustomerRepo customerRepo;

    public CustomerDetailServicesImp(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo ;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) {

        var user = customerRepo.findByUsername(username);

        if (user != null) {
            return User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        }
        return User.withUsername(username)
                .password("{noop}")
                .roles("USER")
                .build();
    }
}


