package pl.tjanek.currencyexchangekata.balance;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.tjanek.currencyexchangekata.balance.BalanceFacade.LessThanZeroInitialBalanceException;
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
        if (amount.isLessThanZero()) throw new LessThanZeroInitialBalanceException();
        this.amount = amount;
    }

    void increase(Money amountToIncrease) {
        amount = amount.plus(amountToIncrease);
    }

    void decrease(Money amountToDecrease) {
        amount = amount.minus(amountToDecrease);
    }

    Currency getCurrency() {
        return amount.currency();
    }

}
