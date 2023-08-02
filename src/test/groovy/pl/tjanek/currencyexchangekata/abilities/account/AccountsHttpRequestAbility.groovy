package pl.tjanek.currencyexchangekata.abilities.account

import pl.tjanek.currencyexchangekata.abilities.HttpRequestAbility

trait AccountsHttpRequestAbility extends HttpRequestAbility {

    static String ACCOUNTS_BASE_URL = '/accounts'

}