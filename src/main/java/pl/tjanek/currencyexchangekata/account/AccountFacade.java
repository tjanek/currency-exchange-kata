package pl.tjanek.currencyexchangekata.account;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.tjanek.currencyexchangekata.balance.BalanceFacade;
import pl.tjanek.currencyexchangekata.common.AccountNumber;
import pl.tjanek.currencyexchangekata.common.Money;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class AccountFacade {

    private final BalanceFacade balances;
    private final AccountRepository repository;
    private final ExchangeRateClient exchangeRates;
    private final Clock clock;

    AccountNumber openNewAccount(AccountHolder holder, Money initialBalance) {
        var accountNumber = AccountNumber.generate();
        var account = new CurrencyAccount(accountNumber, holder, clock.instant());
        repository.save(account);

        balances.openBalanceForAccount(accountNumber, Money.PLN(initialBalance.amount()));
        balances.openBalanceForAccount(accountNumber, Money.ZERO_USD);

        return account.number();
    }

    ExchangeRate exchangePLNtoUSD(AccountNumber accountNumber, BigDecimal amount) {
        var today = today();
        var exchange = Exchange.fromPLNtoUSD(exchangeRates, today, amount);

        Money from = exchange.moneyNeeded();
        Money to = exchange.moneyReceived();

        balances.moneyTransfer(accountNumber, from, to);

        return exchange.rate();
    }

    ExchangeRate exchangeUSDtoPLN(AccountNumber accountNumber, BigDecimal amount) {
        var today = today();
        var exchange = Exchange.fromUSDtoPLN(exchangeRates, today, amount);

        Money from = exchange.moneyNeeded();
        Money to = exchange.moneyReceived();

        balances.moneyTransfer(accountNumber, from, to);

        return exchange.rate();
    }

    Optional<CurrencyAccount> getAccount(AccountNumber accountNumber) {
        return repository.get(accountNumber);
    }

    private LocalDate today() {
        return LocalDate.now(clock);
    }

}
