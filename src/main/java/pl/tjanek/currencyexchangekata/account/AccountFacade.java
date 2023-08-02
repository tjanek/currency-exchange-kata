package pl.tjanek.currencyexchangekata.account;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.tjanek.currencyexchangekata.balance.BalanceFacade;
import pl.tjanek.currencyexchangekata.common.AccountNumber;
import pl.tjanek.currencyexchangekata.common.Money;

import java.time.Clock;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class AccountFacade {

    private final BalanceFacade balances;
    private final AccountRepository repository;
    private final Clock clock;

    AccountNumber openNewAccount(AccountHolder holder, Money initialBalance) {
        AccountNumber accountNumber = AccountNumber.generate();
        CurrencyAccount account = new CurrencyAccount(accountNumber, holder, clock.instant());
        repository.save(account);

        return account.number();
    }

    Optional<CurrencyAccount> getAccount(AccountNumber accountNumber) {
        return repository.get(accountNumber);
    }

}
