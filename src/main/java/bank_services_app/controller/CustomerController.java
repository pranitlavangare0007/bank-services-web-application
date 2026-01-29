package bank_services_app.controller;

import bank_services_app.models.Customer;
import bank_services_app.models.LoginRequest;
import bank_services_app.services.CustomerServices;
import bank_services_app.services.JwtService;
import bank_services_app.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @Autowired
    private CustomerServices customerServices;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtService jwtService;



    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Customer customer){
        if(customerServices.getUser(customer.getUsername()) != null){
            return new ResponseEntity<>("Customer already exists", HttpStatus.CONFLICT);
        }
        customer.setRole(Role.CUSTOMER);
        customerServices.saveUser(customer);
        return new ResponseEntity<>("Customer registered successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest  loginRequest){

        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(), loginRequest.password()
                )
        );

        Customer dbUser = customerServices.getUser(loginRequest.username());

        return new ResponseEntity<>(jwtService.generateToken(
                dbUser.getUsername(),
                dbUser.getRole().name()),HttpStatus.OK);
    }
}
