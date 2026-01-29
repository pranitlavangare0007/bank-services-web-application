package bank_services_app.exceptionHandling;

public class InsufficientBalanceException extends BankingExceptions{
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
