package pl.tjanek.currencyexchangekata.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount, Currency currency) {

    public static Money ZERO_USD = Money.USD(BigDecimal.ZERO);

    public static Money PLN(BigDecimal amount) {
        return new Money(amount, Currency.PLN);
    }

    public static Money USD(BigDecimal amount) {
        return new Money(amount, Currency.USD);
    }

    public Money(BigDecimal amount, Currency currency) {
        this.currency = currency;
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Money plus(Money money) {
        return new Money(amount.add(money.amount), currency);
    }

    public Money minus(Money money) {
        return new Money(amount.subtract(money.amount), currency);
    }

    public boolean isLessThanZero() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

}
