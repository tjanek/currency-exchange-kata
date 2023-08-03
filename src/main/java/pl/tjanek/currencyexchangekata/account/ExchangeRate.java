package pl.tjanek.currencyexchangekata.account;

import java.math.BigDecimal;

public record ExchangeRate(BigDecimal value) {

    static final ExchangeRate ONE = new ExchangeRate(BigDecimal.ONE);

}
