package pl.tjanek.currencyexchangekata.balance;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.tjanek.currencyexchangekata.common.AccountNumber;
import pl.tjanek.currencyexchangekata.common.Currency;
import pl.tjanek.currencyexchangekata.common.Money;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
@Getter
class Balance {

    private final AccountNumber number;
    private final Money amount;

    Currency getCurrency() {
        return amount.currency();
    }

}
