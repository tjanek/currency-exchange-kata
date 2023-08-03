package pl.tjanek.currencyexchangekata.abilities.account

import org.springframework.http.ResponseEntity

trait ExchangeMoneyAbility extends AccountsHttpRequestAbility {

    // @formatter:off
    ResponseEntity<Map> exchangePLNtoUSD(String accountNumber, String amount) {
        httpPostRequest("$ACCOUNTS_BASE_URL/$accountNumber/exchange/from/PLN/to/USD?amount=$amount")
    }

    ResponseEntity<Map> exchangeUSDtoPLN(String accountNumber, String amount) {
        httpPostRequest("$ACCOUNTS_BASE_URL/$accountNumber/exchange/from/USD/to/PLN?amount=$amount")
    }
    // @formatter:on

}