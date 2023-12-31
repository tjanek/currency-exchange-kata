package pl.tjanek.currencyexchangekata.specs

import com.github.tomakehurst.wiremock.client.WireMock
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import pl.tjanek.currencyexchangekata.BaseIntegrationSpec
import pl.tjanek.currencyexchangekata.abilities.account.ExchangeMoneyAbility
import pl.tjanek.currencyexchangekata.abilities.account.GetAccountBalancesAbility
import pl.tjanek.currencyexchangekata.abilities.account.OpenNewAccountAbility

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static pl.tjanek.currencyexchangekata.assertions.account.CurrentAccountBalanceAssertion.assertThatCurrentAccountBalance
import static pl.tjanek.currencyexchangekata.assertions.account.MoneyTransferAssertion.assertThatMoneyTransfer

@AutoConfigureWireMock(port = 9055)
class ExchangeMoneyAcceptanceSpec extends BaseIntegrationSpec
    implements OpenNewAccountAbility, GetAccountBalancesAbility, ExchangeMoneyAbility {

    def setup() {
        WireMock.reset()
    }

    // @formatter:off
    def "should exchange PLN to USD with given amount"() {
        given:
            nbpReturnsSomeUSDRate()
        and:
            def accountOpened = openNewAccount()
            def accountNumber = accountOpened.body['accountNumber'] as String
        when:
            def moneyTransferred = exchangePLNtoUSD(accountNumber, "12.56")
        and:
            def accountBalances = getAccountBalances(accountNumber)
        then:
            assertThatMoneyTransfer(moneyTransferred)
                .succeeded()
                .withExchangeRate("4.0612")
        and:
            assertThatCurrentAccountBalance(accountBalances)
                .hasBalanceInPLN("50.49")
                .hasBalanceInUSD("12.56")
    }

    def "should exchange USD to PLN with given amount"() {
        given:
            nbpReturnsSomeUSDRate()
        and:
            def accountOpened = openNewAccount()
            def accountNumber = accountOpened.body['accountNumber'] as String
        when:
            exchangePLNtoUSD(accountNumber, "12.56")
            def moneyTransferred = exchangeUSDtoPLN(accountNumber, "3.54")
        and:
            def accountBalances = getAccountBalances(accountNumber)
        then:
            assertThatMoneyTransfer(moneyTransferred)
                .succeeded()
                .withExchangeRate("4.0612")
        and:
            assertThatCurrentAccountBalance(accountBalances)
                .hasBalanceInPLN("54.03")
                .hasBalanceInUSD("11.69")
    }
    // @formatter:on

    def "should not exchange with given amount bigger than money has"() {
        given:
            nbpReturnsSomeUSDRate()
        and:
            def accountOpened = openNewAccount()
            def accountNumber = accountOpened.body['accountNumber'] as String
        when:
            def moneyTransferred = exchangePLNtoUSD(accountNumber, "25.00")
        then:
            assertThatMoneyTransfer(moneyTransferred)
                .notTransferredBecauseOfNotEnoughMoney()
    }

    def "should not exchange when NBP is not available"() {
        given:
            nbpIsNotAvailable()
        and:
            def accountOpened = openNewAccount()
            def accountNumber = accountOpened.body['accountNumber'] as String
        when:
            def moneyTransferred = exchangePLNtoUSD(accountNumber, "12.56")
        then:
            assertThatMoneyTransfer(moneyTransferred)
                .notTransferredBecauseOfNbpApiNotAvailable()
    }

    def "should not exchange when NBP returns empty USD rate"() {
        given:
            nbpReturnsEmptyUSDRate()
        and:
            def accountOpened = openNewAccount()
            def accountNumber = accountOpened.body['accountNumber'] as String
        when:
            def moneyTransferred = exchangePLNtoUSD(accountNumber, "12.56")
        then:
            assertThatMoneyTransfer(moneyTransferred)
                .notTransferredBecauseOfRateNotFound()
    }

    private static void nbpReturnsSomeUSDRate() {
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/USD/?format=json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
{
  "table": "A",
  "currency": "dolar amerykański",
  "code": "USD",
  "rates": [
    {
      "no": "149/A/NBP/2023",
      "effectiveDate": "2023-08-03",
      "mid": 4.0612
    }
  ]
}
""")))
    }

    private static void nbpReturnsEmptyUSDRate() {
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/USD/?format=json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
{
  "table": "A",
  "currency": "dolar amerykański",
  "code": "USD",
  "rates": []
}
""")))
    }

    private static void nbpIsNotAvailable() {
        stubFor(get(urlEqualTo("/api/exchangerates/rates/a/USD/?format=json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(500)))
    }

}
