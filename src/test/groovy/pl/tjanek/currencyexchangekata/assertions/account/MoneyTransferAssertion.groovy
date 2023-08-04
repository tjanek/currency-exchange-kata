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

    MoneyTransferAssertion notTransferredBecauseOfNotEnoughMoney() {
        assert result.statusCode == HttpStatus.UNPROCESSABLE_ENTITY
        assert result.body['error'] == 'Could not transfer money because of not enough money'
        this
    }

    MoneyTransferAssertion notTransferredBecauseOfNbpApiNotAvailable() {
        assert result.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        assert result.body['error'] == 'Error when try to exchange money'
        this
    }

    MoneyTransferAssertion notTransferredBecauseOfRateNotFound() {
        assert result.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        assert result.body['error'] == 'Error when try to exchange money'
        this
    }

    MoneyTransferAssertion withExchangeRate(String rate) {
        assert result.body['exchangeRate'] as String == rate
        this
    }

}
