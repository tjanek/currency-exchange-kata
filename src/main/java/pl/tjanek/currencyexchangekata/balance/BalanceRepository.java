package pl.tjanek.currencyexchangekata.balance;

import pl.tjanek.currencyexchangekata.common.AccountNumber;
import pl.tjanek.currencyexchangekata.common.Currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BalanceRepository {

    private final Map<AccountNumber, Map<Currency, Balance>> balances = new HashMap<>();

    void save(Balance balance) {
        if (balances.containsKey(balance.getNumber())) {
            var currencyBalance = balances.get(balance.getNumber());
            currencyBalance.put(balance.getAmount().currency(), balance);
        } else {
            var currencyBalance = new HashMap<Currency, Balance>();
            currencyBalance.put(balance.getAmount().currency(), balance);
            balances.put(balance.getNumber(), currencyBalance);
        }
    }

    List<Balance> getBalancesFor(AccountNumber accountNumber) {
        return new ArrayList<>(
                balances.getOrDefault(accountNumber, new HashMap<>()).values()
        );
    }

    Balance getBalance(AccountNumber accountNumber, Currency currency) {
        return getBalancesFor(accountNumber).stream()
                .filter(balance -> currency.equals(balance.getCurrency()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found balance for given account number and currency"));
    }

}
