package pl.tjanek.currencyexchangekata.specs


import pl.tjanek.currencyexchangekata.BaseIntegrationSpec
import pl.tjanek.currencyexchangekata.abilities.account.GetAccountDetailsAbility
import pl.tjanek.currencyexchangekata.abilities.account.OpenNewAccountAbility

import static pl.tjanek.currencyexchangekata.assertions.account.GetOpenedAccountDetailsAssertion.assertThatAccount
import static pl.tjanek.currencyexchangekata.assertions.account.OpenNewAccountAssertion.assertThatOpeningNewAccount

class OpenNewAccountAcceptanceSpec extends BaseIntegrationSpec
    implements OpenNewAccountAbility, GetAccountDetailsAbility {

    // @formatter:off
    def "should open new account with given PLN initial balance"() {
        when:
            def accountOpened = openNewAccount()
            def accountNumber = accountOpened.body['accountNumber'] as String
        and:
            def accountDetailsResponse = getAccountDetails(accountOpened)
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
    }
    // @formatter:on

}
