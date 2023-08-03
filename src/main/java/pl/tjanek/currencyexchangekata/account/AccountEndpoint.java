package pl.tjanek.currencyexchangekata.account;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.tjanek.currencyexchangekata.common.AccountNumber;
import pl.tjanek.currencyexchangekata.common.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class AccountEndpoint {

    private final AccountFacade accounts;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    OpenNewAccountResponse openNewAccount(@RequestBody OpenNewAccountRequest request) {
        AccountHolder accountHolder = new AccountHolder(request.firstName(), request.lastName());
        Money initialBalance = Money.PLN(request.initialBalance());
        AccountNumber accountNumber = accounts.openNewAccount(accountHolder, initialBalance);

        return new OpenNewAccountResponse(accountNumber.getNumber());
    }

    @GetMapping("/{accountNumber}")
    ResponseEntity<AccountDetailsDto> getAccountDetails(@PathVariable UUID accountNumber) {
        return accounts.getAccount(AccountNumber.of(accountNumber))
                .map((account) -> ResponseEntity.ok(AccountDetailsDto.from(account)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

record OpenNewAccountRequest(String firstName, String lastName, BigDecimal initialBalance) {}

record OpenNewAccountResponse(UUID accountNumber) {}

record AccountDetailsDto(UUID accountNumber, AccountHolderDto holder, Instant openedAt) {

    static AccountDetailsDto from(CurrencyAccount account) {
        return new AccountDetailsDto(
            account.number().getNumber(),
            AccountHolderDto.from(account.holder()),
            account.openedAt()
        );
    }

}

record AccountHolderDto(String firstName, String lastName) {

    static AccountHolderDto from(AccountHolder holder) {
        return new AccountHolderDto(
                holder.firstName(), holder.lastName()
        );
    }

}
