package pl.tjanek.currencyexchangekata.specs


import pl.tjanek.currencyexchangekata.BaseIntegrationSpec
import pl.tjanek.currencyexchangekata.abilities.account.GetAccountBalancesAbility
import pl.tjanek.currencyexchangekata.abilities.account.GetAccountDetailsAbility
import pl.tjanek.currencyexchangekata.abilities.account.OpenNewAccountAbility

import static pl.tjanek.currencyexchangekata.assertions.account.CurrentAccountBalanceAssertion.assertThatCurrentAccountBalance
import static pl.tjanek.currencyexchangekata.assertions.account.GetOpenedAccountDetailsAssertion.assertThatAccount
import static pl.tjanek.currencyexchangekata.assertions.account.OpenNewAccountAssertion.assertThatOpeningNewAccount

class OpenNewAccountAcceptanceSpec extends BaseIntegrationSpec
    implements OpenNewAccountAbility, GetAccountDetailsAbility, GetAccountBalancesAbility {

    // @formatter:off
    def "should open new account with given PLN initial balance"() {
        when:
            def accountOpened = openNewAccount()
            def accountNumber = accountOpened.body['accountNumber'] as String
        and:
            def accountDetailsResponse = getAccountDetails(accountOpened)
        and:
            def accountBalances = getAccountBalances(accountNumber)
        then:
            assertThatOpeningNewAccount(accountOpened)
                .succeeded()
                .accountNumberGenerated()
        and:
            assertThatAccount(accountDetailsResponse)
                .isOpen()
                .isOpenedAt(NOW)
                .hasAccountNumber(accountNumber)
                .isHoldBy('Jan', 'Kowalski')
        and:
            assertThatCurrentAccountBalance(accountBalances)
                .hasBalanceInPLN("101.5")
                .hasBalanceInUSD("0.0")
    }

    def "should not find not open account yet"() {
        given:
            def accountNumber = UUID.randomUUID().toString()
        when:
            def accountDetailsResponse = getAccountDetails(accountNumber)
        then:
            assertThatAccount(accountDetailsResponse)
                .isNotOpen()
    }
    // @formatter:on

}
