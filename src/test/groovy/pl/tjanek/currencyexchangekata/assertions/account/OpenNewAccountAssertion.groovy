package pl.tjanek.currencyexchangekata.assertions.account

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import pl.tjanek.currencyexchangekata.assertions.CommonAssertion

class OpenNewAccountAssertion extends CommonAssertion {

    private final ResponseEntity<Map> result

    static OpenNewAccountAssertion assertThatOpeningNewAccount(ResponseEntity<Map> response) {
        return new OpenNewAccountAssertion(response)
    }

    private OpenNewAccountAssertion(ResponseEntity<Map> response) {
        this.result = response
    }

    OpenNewAccountAssertion succeeded() {
        assert result.statusCode == HttpStatus.CREATED
        this
    }

    OpenNewAccountAssertion accountNumberGenerated() {
        assert result.body['accountNumber'] != null
        assertThatIsUUID(result.body['accountNumber'])
        this
    }


}
