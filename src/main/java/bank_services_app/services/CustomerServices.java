package bank_services_app.services;

import bank_services_app.models.Customer;
import bank_services_app.repositry.CustomerRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerServices {
    @Autowired
    CustomerRepo customerRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);



    public Customer getUser(String username) {

        return customerRepo.findByUsername(username);
    }

    public Customer saveUser(@NonNull Customer customer) {
        customer.setPassword(encoder.encode(customer.getPassword()));
        return customerRepo.save(customer);
    }
}
