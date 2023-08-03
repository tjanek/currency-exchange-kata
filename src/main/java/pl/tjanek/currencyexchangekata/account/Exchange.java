package pl.tjanek.currencyexchangekata.account;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.tjanek.currencyexchangekata.common.Currency;
import pl.tjanek.currencyexchangekata.common.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class Exchange {

    private final ExchangeRateClient rateClient;
    private final LocalDate exchangeEffectiveDate;
    private final Currency fromCurrency;
    private final Currency toCurrency;
    private final BigDecimal amount;
    private ExchangeRate rate;

    static Exchange fromPLNtoUSD(ExchangeRateClient rateClient,
                                 LocalDate exchangeEffectiveDate,
                                 BigDecimal amount) {
        return new Exchange(rateClient, exchangeEffectiveDate, Currency.PLN, Currency.USD, amount);
    }

    static Exchange fromUSDtoPLN(ExchangeRateClient rateClient,
                                 LocalDate exchangeEffectiveDate,
                                 BigDecimal amount) {
        return new Exchange(rateClient, exchangeEffectiveDate, Currency.USD, Currency.PLN, amount);
    }

    Money moneyNeeded() {
        if (Currency.PLN == fromCurrency) {
            rate = rateClient.getExchangeRateFromPLN(exchangeEffectiveDate, toCurrency);
            return new Money(amount.multiply(rate.value()), Currency.PLN);
        } else {
            rate = rateClient.getExchangeRateFromPLN(exchangeEffectiveDate, fromCurrency);
            return new Money(amount.divide(rate.value(), RoundingMode.HALF_EVEN), fromCurrency);
        }
    }

    Money moneyReceived() {
        return new Money(amount, toCurrency);
    }

    ExchangeRate rate() {
        return rate;
    }

}
