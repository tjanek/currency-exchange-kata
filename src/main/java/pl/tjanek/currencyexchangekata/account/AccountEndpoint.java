package pl.tjanek.currencyexchangekata.account;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.tjanek.currencyexchangekata.balance.BalanceFacade.NegativeInitialBalanceException;
import pl.tjanek.currencyexchangekata.balance.BalanceFacade.NotEnoughMoneyException;
import pl.tjanek.currencyexchangekata.common.AccountNumber;
import pl.tjanek.currencyexchangekata.common.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class AccountEndpoint {

    private final AccountFacade accounts;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    OpenNewAccountResponse openNewAccount(@RequestBody OpenNewAccountRequest request) {
        var accountHolder = new AccountHolder(request.firstName(), request.lastName());
        var initialBalance = Money.PLN(request.initialBalance());
        var accountNumber = accounts.openNewAccount(accountHolder, initialBalance);

        return new OpenNewAccountResponse(accountNumber.getNumber());
    }

    @GetMapping("/{accountNumber}")
    ResponseEntity<AccountDetailsDto> getAccountDetails(@PathVariable UUID accountNumber) {
        return accounts.getAccount(AccountNumber.of(accountNumber))
                .map((account) -> ResponseEntity.ok(AccountDetailsDto.from(account)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{accountNumber}/exchange/from/PLN/to/USD")
    MoneyTransferResponse exchangePLNtoUSD(@PathVariable UUID accountNumber, @RequestParam BigDecimal amount) {
        ExchangeRate exchangeRate = accounts.exchangePLNtoUSD(AccountNumber.of(accountNumber), amount);
        return new MoneyTransferResponse(exchangeRate.value());
    }

    @PostMapping("/{accountNumber}/exchange/from/USD/to/PLN")
    MoneyTransferResponse exchangeUSDtoPLN(@PathVariable UUID accountNumber, @RequestParam BigDecimal amount) {
        ExchangeRate exchangeRate = accounts.exchangeUSDtoPLN(AccountNumber.of(accountNumber), amount);
        return new MoneyTransferResponse(exchangeRate.value());
    }

    @ExceptionHandler(NegativeInitialBalanceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Map<String, String>> handleNegativeInitialBalanceException(NegativeInitialBalanceException exception) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("error", "Could not create account with negative initial balance"));
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Map<String, String>> handleNotEnoughMoneyException(NotEnoughMoneyException exception) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("error", "Could not transfer money because of not enough money"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("error", "Error"));
    }

}

record OpenNewAccountRequest(String firstName, String lastName, BigDecimal initialBalance) {}

record OpenNewAccountResponse(UUID accountNumber) {}

record MoneyTransferResponse(BigDecimal exchangeRate) {}

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
