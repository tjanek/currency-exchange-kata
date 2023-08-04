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

    public void moneyTransfer(AccountNumber accountNumber, Money from, Money to) {
        var fromBalance = repository.getBalance(accountNumber, from.currency());
        fromBalance.decrease(from);

        var toBalance = repository.getBalance(accountNumber, to.currency());
        toBalance.increase(to);

        repository.save(fromBalance);
        repository.save(toBalance);
    }

    List<Balance> getBalances(AccountNumber accountNumber) {
        return repository.getBalancesFor(accountNumber);
    }

    public static class LessThanZeroInitialBalanceException extends RuntimeException { }
}
