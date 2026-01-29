package bank_services_app.models;

import java.math.BigDecimal;

public record TransactionRequest(String mpin , BigDecimal amount) {
}
