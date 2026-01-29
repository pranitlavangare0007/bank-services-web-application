package bank_services_app.repositry;

import bank_services_app.models.AccountDetails;
import bank_services_app.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepo extends JpaRepository<AccountDetails,Long> {

    AccountDetails findByCustomerAndAccountNumber(Customer customer ,String accountNumber);
   List<AccountDetails> findAllByCustomer(Customer customer);

}
