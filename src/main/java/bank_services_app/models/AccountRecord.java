package bank_services_app.models;

import bank_services_app.util.AccountPurpose;
import bank_services_app.util.AccountType;

public record AccountRecord(String mpin, AccountType accountType , AccountPurpose accountPurpose) {
}
