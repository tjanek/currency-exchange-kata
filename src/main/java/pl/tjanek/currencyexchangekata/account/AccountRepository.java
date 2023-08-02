package pl.tjanek.currencyexchangekata.account;

import pl.tjanek.currencyexchangekata.common.AccountNumber;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class AccountRepository {

    private final Map<AccountNumber, CurrencyAccount> accounts = new HashMap<>();

    void save(CurrencyAccount account) {
        accounts.put(account.number(), account);
    }

    Optional <CurrencyAccount> get(AccountNumber accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

}
