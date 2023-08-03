package pl.tjanek.currencyexchangekata.assertions.account


import org.springframework.http.ResponseEntity

class CurrentAccountBalanceAssertion {

    private final ResponseEntity<Map> result

    static CurrentAccountBalanceAssertion assertThatCurrentAccountBalance(ResponseEntity<Map> response) {
        return new CurrentAccountBalanceAssertion(response)
    }

    private CurrentAccountBalanceAssertion(ResponseEntity<Map> response) {
        this.result = response
    }

    CurrentAccountBalanceAssertion hasBalanceInPLN(String balance) {
        assert result.body['balances']['PLN'] as String == balance
        this
    }

    CurrentAccountBalanceAssertion hasBalanceInUSD(String balance) {
        assert result.body['balances']['USD'] as String == balance
        this
    }

}
