package pl.tjanek.currencyexchangekata.balance;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.tjanek.currencyexchangekata.common.AccountNumber;
import pl.tjanek.currencyexchangekata.common.Money;

import java.util.List;


@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BalanceFacade {

    private final BalanceRepository repository;

    public void openBalanceForAccount(AccountNumber accountNumber, Money initialBalance) {
        var balance = new Balance(accountNumber, initialBalance);
        repository.save(balance);
    }

    List<Balance> getBalances(AccountNumber accountNumber) {
        return repository.getBalancesFor(accountNumber);
    }

}
