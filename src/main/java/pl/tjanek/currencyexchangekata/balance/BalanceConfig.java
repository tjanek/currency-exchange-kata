package pl.tjanek.currencyexchangekata.balance;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BalanceConfig {

    @Bean
    BalanceFacade defaultBalances(BalanceRepository repository) {
        return new BalanceFacade(repository);
    }

    @Bean
    BalanceRepository inMemoryBalanceRepository() {
        return new BalanceRepository();
    }

}
