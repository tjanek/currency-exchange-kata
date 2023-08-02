package pl.tjanek.currencyexchangekata.abilities.account

import org.springframework.http.ResponseEntity

trait OpenNewAccountAbility extends AccountsHttpRequestAbility {

    // @formatter:off
    ResponseEntity<Map> openNewAccount(
            String firstName = 'Jan',
            String lastName = 'Kowalski',
            String initialBalancePLN = "100.00") {

        httpPostRequest(ACCOUNTS_BASE_URL, [
                firstName: firstName,
                lastName: lastName,
                initialBalance: initialBalancePLN
        ])
    }
    // @formatter:on

}