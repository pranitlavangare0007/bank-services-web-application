package bank_services_app.exceptionHandling;

public class AccountDeactivatedException extends BankingExceptions{
    public AccountDeactivatedException(String message) {
        super(message);
    }
}
