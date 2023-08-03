package pl.tjanek.currencyexchangekata.account.adapters.nbp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import pl.tjanek.currencyexchangekata.account.ExchangeRateClient;

@Configuration
class NbpExchangeRateHttpClientConfig {

    @Value("${nbp.url}")
    private String nbpApiBaseUrl;

    @Bean
    @Profile("nbp-exchange-rate")
    ExchangeRateClient nbpExchangeRateHttpClient(RestTemplate nbpExchangeRateRestTemplate) {
        return new NbpExchangeRateHttpClient(nbpExchangeRateRestTemplate, nbpApiBaseUrl);
    }

    @Bean
    @Profile("nbp-exchange-rate")
    RestTemplate nbpExchangeRateRestTemplate() {
        return new RestTemplate();
    }

}
