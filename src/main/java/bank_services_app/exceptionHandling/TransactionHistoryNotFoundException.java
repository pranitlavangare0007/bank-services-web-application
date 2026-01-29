package bank_services_app.exceptionHandling;

public class TransactionHistoryNotFoundException extends BankingExceptions{
    public TransactionHistoryNotFoundException(String message) {
        super(message);
    }
}
