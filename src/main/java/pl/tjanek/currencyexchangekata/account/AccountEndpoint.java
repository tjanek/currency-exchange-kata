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
    ResponseEntity<AccountDetailsView> getAccountDetails(@PathVariable UUID accountNumber) {
        // TODO: throw 404 when not found
        CurrencyAccount account = accounts.getAccount(AccountNumber.of(accountNumber)).get();

        return ResponseEntity.ok(AccountDetailsView.from(account));
    }

}

record OpenNewAccountRequest(String firstName, String lastName, BigDecimal initialBalance) {}

record OpenNewAccountResponse(UUID accountNumber) {}

record AccountDetailsView(UUID accountNumber, AccountHolderView holder, Instant openedAt) {

    static AccountDetailsView from(CurrencyAccount account) {
        return new AccountDetailsView(
            account.number().getNumber(),
            AccountHolderView.from(account.holder()),
            account.openedAt()
        );
    }

}

record AccountHolderView(String firstName, String lastName) {

    static AccountHolderView from(AccountHolder holder) {
        return new AccountHolderView(
                holder.firstName(), holder.lastName()
        );
    }

}
