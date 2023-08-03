package pl.tjanek.currencyexchangekata.assertions.account

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import java.time.Instant

class GetOpenedAccountDetailsAssertion {

    private final ResponseEntity<Map> result

    static GetOpenedAccountDetailsAssertion assertThatAccount(ResponseEntity<Map> response) {
        return new GetOpenedAccountDetailsAssertion(response)
    }

    private GetOpenedAccountDetailsAssertion(ResponseEntity<Map> response) {
        this.result = response
    }

    GetOpenedAccountDetailsAssertion isOpen() {
        assert result.statusCode == HttpStatus.OK
        this
    }

    GetOpenedAccountDetailsAssertion isNotOpen() {
        assert result.statusCode == HttpStatus.NOT_FOUND
        this
    }

    GetOpenedAccountDetailsAssertion hasAccountNumber(String accountNumber) {
        assert result.body['accountNumber'] == accountNumber
        this
    }

    GetOpenedAccountDetailsAssertion isOpenedAt(Instant openedAt) {
        assert result.body['openedAt'] == openedAt as String
        this
    }

    GetOpenedAccountDetailsAssertion isHoldBy(String holderFirstName, String holderLastName) {
        assert result.body['holder']['firstName'] == holderFirstName
        assert result.body['holder']['lastName'] == holderLastName
        this
    }

}
