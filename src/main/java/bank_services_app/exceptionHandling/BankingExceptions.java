package bank_services_app.exceptionHandling;

public abstract class BankingExceptions extends RuntimeException{

    public BankingExceptions(String message){
        super(message);
    }

}
