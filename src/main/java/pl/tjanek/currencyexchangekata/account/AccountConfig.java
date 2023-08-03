package pl.tjanek.currencyexchangekata.account;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tjanek.currencyexchangekata.balance.BalanceFacade;

import java.time.Clock;

@Configuration
class AccountConfig {

    @Bean
    AccountFacade defaultAccountFacade(AccountRepository repository,
                                       BalanceFacade balances,
                                       ExchangeRateClient exchangeRateClient,
                                       Clock clock) {
        return new AccountFacade(balances, repository, exchangeRateClient, clock);
    }

    @Bean
    AccountRepository inMemoryRepository() {
        return new AccountRepository();
    }

}
