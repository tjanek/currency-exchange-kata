package pl.tjanek.currencyexchangekata.abilities.account

import org.springframework.http.ResponseEntity

trait GetAccountBalancesAbility extends AccountsHttpRequestAbility {

    // @formatter:off
    ResponseEntity<Map> getAccountBalances(String accountNumber) {
        httpGetRequest("$ACCOUNTS_BASE_URL/$accountNumber/balances")
    }
    // @formatter:on

}