package pl.tjanek.currencyexchangekata.balance;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.tjanek.currencyexchangekata.common.AccountNumber;
import pl.tjanek.currencyexchangekata.common.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class BalanceEndpoint {

    private final BalanceFacade balances;

    @GetMapping("/accounts/{accountNumber}/balances")
    AccountBalancesDto getAccountBalances(@PathVariable UUID accountNumber) {
        return AccountBalancesDto.from(balances.getBalances(AccountNumber.of(accountNumber)));
    }

}

record AccountBalancesDto(Map<Currency, BigDecimal> balances) {

    static AccountBalancesDto from(List<Balance> balances) {
        var currencyBalances = balances.stream()
                .collect(Collectors.toMap(
                        Balance::getCurrency,
                        (balance) -> balance.getAmount().amount()));
        return new AccountBalancesDto(currencyBalances);
    }

}
