package pl.tjanek.currencyexchangekata.abilities.account

import org.springframework.http.ResponseEntity

trait GetAccountDetailsAbility extends AccountsHttpRequestAbility {

    // @formatter:off
    ResponseEntity<Map> getAccountDetails(ResponseEntity<Map> accountOpenedResponse) {
        String accountNumber = accountOpenedResponse.body['accountNumber']
        getAccountDetails(accountNumber)
    }

    ResponseEntity<Map> getAccountDetails(String accountNumber) {
        httpGetRequest("$ACCOUNTS_BASE_URL/$accountNumber")
    }
    // @formatter:on

}