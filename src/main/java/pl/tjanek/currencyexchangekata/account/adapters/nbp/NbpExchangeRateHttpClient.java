package pl.tjanek.currencyexchangekata.account.adapters.nbp;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import pl.tjanek.currencyexchangekata.account.ExchangeRate;
import pl.tjanek.currencyexchangekata.account.ExchangeRateClient;
import pl.tjanek.currencyexchangekata.common.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
class NbpExchangeRateHttpClient implements ExchangeRateClient {

    private static final String NBP_API_RATE_URL = "/api/exchangerates/rates/a/%s/?format=json";

    private final RestTemplate restTemplate;

    private final String nbpApiBaseUrl;

    private final Map<SimpleExchangeRatesCacheKey, ExchangeRate> cache = new HashMap<>();

    @Override
    public ExchangeRate getExchangeRateFromPLN(LocalDate effectiveDate, Currency toCurrency) {
        SimpleExchangeRatesCacheKey cacheKey = new SimpleExchangeRatesCacheKey(effectiveDate, toCurrency);

        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        String url = getExchangeRateUrl(toCurrency);
        NbpApiExchangeRateResponse response = restTemplate.getForObject(url, NbpApiExchangeRateResponse.class);

        BigDecimal value = response.rates().stream()
                .map(NbpApiExchangeRateResponse.Rate::mid)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "[NBP REST API] Could not find exchange rate for " + toCurrency));

        ExchangeRate exchangeRate = new ExchangeRate(value);
        cache.put(cacheKey, exchangeRate);

        return exchangeRate;
    }

    private String getExchangeRateUrl(Currency currency) {
        return nbpApiBaseUrl + String.format(NBP_API_RATE_URL, currency.name());
    }

}

record NbpApiExchangeRateResponse(String code, List<Rate> rates) {
    record Rate(BigDecimal mid) {}
}

record SimpleExchangeRatesCacheKey(LocalDate effectiveDate, Currency toCurrency) {}
