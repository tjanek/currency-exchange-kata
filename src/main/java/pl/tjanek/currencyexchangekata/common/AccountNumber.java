package pl.tjanek.currencyexchangekata.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@EqualsAndHashCode(of = "number")
@Getter
public final class AccountNumber {

    private final UUID number;

    public static AccountNumber generate() {
        return new AccountNumber(UUID.randomUUID());
    }

    public static AccountNumber of(UUID number) {
        return new AccountNumber(number);
    }

    private AccountNumber(UUID number) {
        this.number = number;
    }

}
