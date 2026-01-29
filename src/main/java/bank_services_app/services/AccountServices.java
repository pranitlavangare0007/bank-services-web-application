package bank_services_app.services;

import bank_services_app.Dto.AccountResponse;

import bank_services_app.Dto.BalanceResponse;
import bank_services_app.exceptionHandling.*;
import bank_services_app.models.AccountDetails;
import bank_services_app.models.AccountRecord;
import bank_services_app.models.Customer;
import bank_services_app.models.TransactionRequest;
import bank_services_app.repositry.AccountRepo;
import bank_services_app.util.AccountStatus;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountServices {

private final AccountRepo accountRepo;
public AccountServices(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
}

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public String generateAccountNumber(){
        LocalTime timestamp = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String time = timestamp.format(formatter);
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);

    return "AC"+time+random;
}
    public void verifyAccount(AccountDetails accountDetails){
        if(accountDetails==null){
            throw new AccountNotFoundException("Account not found");
        }

    }
    public void verifyMpin(AccountDetails account, String mpin) {

        if (mpin ==null || !encoder.matches(mpin, account.getMpin())) {
            throw new InvalidMpin("Invalid Mpin");
        }
    }
    public void verifyAmount(BigDecimal amount) {
        if( amount==null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new NegativeAmountException("Invalid Amount");
        }
    }

    public void verifyAccountStatus(AccountStatus accountStatus){
        if(accountStatus == AccountStatus.DEACTIVATE){
            throw new AccountDeactivatedException("Account is deactivated");
        }

    }

    public void verifyWithdraw(AccountDetails accountDetails, BigDecimal amount) {
        if(  accountDetails.getBalance().compareTo(amount)< 0 ){
            throw new InsufficientBalanceException("Insufficient Balance");
        }
    }

    public void verifyMpinFormat(String mpin){
        if (mpin == null || !mpin.matches("\\d{6}")) {
            throw new InvalidMpin("MPIN must be 6 digits");
        }
    }

    public AccountResponse openAccount(AccountRecord accountRecord, Customer customer) {
        AccountDetails accountDetails = new AccountDetails();

        verifyMpinFormat(accountRecord.mpin());

        accountDetails.setAccountNumber(generateAccountNumber());
        accountDetails.setAccountType(accountRecord.accountType());
        accountDetails.setMpin(encoder.encode(accountRecord.mpin()));
        accountDetails.setCustomerId(customer);
        accountDetails.setAccountPurpose(accountRecord.accountPurpose());

        accountDetails =  accountRepo.save(accountDetails);
          return new AccountResponse(
                accountDetails.getAccountNumber(),
                accountDetails.getAccountType(),
                accountDetails.getAccountStatus(),
                accountDetails.getAccountPurpose(),
                accountDetails.getBalance()
        );
    }

    public AccountResponse getAccount(Customer customer,String accountNumber) {

   AccountDetails accountDetails =accountRepo.findByCustomerAndAccountNumber(customer,accountNumber);
   verifyAccount(accountDetails);
        return new AccountResponse(
                accountDetails.getAccountNumber(),
                accountDetails.getAccountType(),
                accountDetails.getAccountStatus(),
                accountDetails.getAccountPurpose(),
                accountDetails.getBalance()
        );
    }

    public List<AccountResponse> getAllAccount(Customer customer) {
        List<AccountDetails> accounts =accountRepo.findAllByCustomer(customer);

        return accounts.stream()
                .map(accountDetails-> new AccountResponse(
                        accountDetails.getAccountNumber(),
                        accountDetails.getAccountType(),
                        accountDetails.getAccountStatus(),
                        accountDetails.getAccountPurpose(),
                        accountDetails.getBalance()
                ))
                .toList();

    }

    @Transactional
    public void deposit(String accountNumber, TransactionRequest request ,Customer customer) {
    AccountDetails accountDetails =accountRepo.findByCustomerAndAccountNumber(customer,accountNumber);

    verifyAccount(accountDetails);
    verifyAccountStatus(accountDetails.getAccountStatus());
    verifyAmount(request.amount());
    verifyMpin(accountDetails, request.mpin());

    accountDetails.setBalance(accountDetails.getBalance().add(request.amount()));

    }

    @Transactional
    public void withdraw(String accountNumber, TransactionRequest request, Customer customer) {

        AccountDetails accountDetails =accountRepo.findByCustomerAndAccountNumber(customer,accountNumber);

        verifyAccount(accountDetails);
        verifyAccountStatus(accountDetails.getAccountStatus());
        verifyAmount(request.amount());
        verifyMpin(accountDetails, request.mpin());
        verifyWithdraw(accountDetails,request.amount());

        accountDetails.setBalance(accountDetails.getBalance().subtract(request.amount()));

    }

    public BalanceResponse getBalance(Customer customer, String accountNumber) {
        AccountDetails accountDetails =accountRepo.findByCustomerAndAccountNumber(customer,accountNumber);
        verifyAccount(accountDetails);
        verifyAccountStatus(accountDetails.getAccountStatus());
        return new BalanceResponse(
                accountDetails.getAccountNumber(),
                accountDetails.getBalance());
    }
}
