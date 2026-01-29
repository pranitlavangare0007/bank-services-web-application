package bank_services_app.exceptionHandling;

public class AccountNotFoundException extends BankingExceptions{
    public AccountNotFoundException(String message) {
        super(message);
    }
}
