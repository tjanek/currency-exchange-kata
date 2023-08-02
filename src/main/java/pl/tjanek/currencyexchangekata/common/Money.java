package pl.tjanek.currencyexchangekata.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {

    private final Currency currency;
    private final BigDecimal amount;

    public static Money PLN(BigDecimal amount) {
        return new Money(Currency.PLN, amount);
    }

    public static Money USD(BigDecimal amount) {
        return new Money(Currency.USD, amount);
    }

    private Money(Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Money plus(Money money) {
        return new Money(currency, amount.add(money.amount));
    }

    public Money minus(Money money) {
        return new Money(currency, amount.subtract(money.amount));
    }

}
