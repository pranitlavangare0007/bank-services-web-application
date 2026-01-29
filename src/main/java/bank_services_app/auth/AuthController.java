

package bank_services_app.auth;

import bank_services_app.services.JwtService;
import bank_services_app.util.Role;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @GetMapping("/oauth-success")
    public void oauthSuccess(HttpServletResponse response) throws IOException {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            response.sendRedirect("http://localhost:8080/login");
            return;
        }
        String email = auth.getName();

        String token = jwtService.generateToken(email, Role.CUSTOMER.name());
        response.sendRedirect(
                "http://localhost:8080" + token
        );

    }


}
