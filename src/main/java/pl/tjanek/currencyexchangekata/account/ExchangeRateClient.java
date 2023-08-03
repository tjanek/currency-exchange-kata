package pl.tjanek.currencyexchangekata.account;

import pl.tjanek.currencyexchangekata.common.Currency;

import java.time.LocalDate;

public interface ExchangeRateClient {

    ExchangeRate getExchangeRateFromPLN(LocalDate effectiveDate, Currency toCurrency);

}
