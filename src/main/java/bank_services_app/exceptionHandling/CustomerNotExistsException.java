package bank_services_app.exceptionHandling;

public class CustomerNotExistsException extends  BankingExceptions{
    public CustomerNotExistsException(String message) {
        super(message);
    }
}
