package bank_services_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hellow {

    @GetMapping("/")
    public String hello(){
        return  "Hello World";
    }
}
