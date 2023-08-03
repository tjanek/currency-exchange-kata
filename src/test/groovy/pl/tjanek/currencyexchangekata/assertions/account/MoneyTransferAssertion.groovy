package pl.tjanek.currencyexchangekata.assertions.account

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class MoneyTransferAssertion {

    private final ResponseEntity<Map> result

    static MoneyTransferAssertion assertThatMoneyTransfer(ResponseEntity<Map> response) {
        return new MoneyTransferAssertion(response)
    }

    private MoneyTransferAssertion(ResponseEntity<Map> response) {
        this.result = response
    }

    MoneyTransferAssertion succeeded() {
        assert result.statusCode == HttpStatus.OK
        this
    }

    MoneyTransferAssertion withExchangeRate(String rate) {
        assert result.body['exchangeRate'] as String == rate
        this
    }

}
