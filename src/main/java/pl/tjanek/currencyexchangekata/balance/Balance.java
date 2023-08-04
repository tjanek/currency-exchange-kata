package pl.tjanek.currencyexchangekata.balance;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.tjanek.currencyexchangekata.balance.BalanceFacade.NegativeInitialBalanceException;
import pl.tjanek.currencyexchangekata.balance.BalanceFacade.NotEnoughMoneyException;
import pl.tjanek.currencyexchangekata.common.AccountNumber;
import pl.tjanek.currencyexchangekata.common.Currency;
import pl.tjanek.currencyexchangekata.common.Money;

@EqualsAndHashCode
@Getter
class Balance {

    private final AccountNumber number;
    private Money amount;

    Balance(AccountNumber number, Money amount) {
        this.number = number;
        if (amount.isLessThanZero()) throw new NegativeInitialBalanceException();
        this.amount = amount;
    }

    void increase(Money amountToIncrease) {
        amount = amount.plus(amountToIncrease);
    }

    void decrease(Money amountToDecrease) {
        Money amountAfterDecrease = amount.minus(amountToDecrease);
        if (amountAfterDecrease.isLessThanZero()) throw new NotEnoughMoneyException();
        amount = amountAfterDecrease;
    }

    Currency getCurrency() {
        return amount.currency();
    }

}
