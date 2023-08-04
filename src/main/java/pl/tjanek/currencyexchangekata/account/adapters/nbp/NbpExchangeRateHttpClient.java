package pl.tjanek.currencyexchangekata.account.adapters.nbp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
class NbpExchangeRateHttpClient implements ExchangeRateClient {

    private static final String NBP_API_RATE_URL = "/api/exchangerates/rates/a/%s/?format=json";

    private final RestTemplate restTemplate;

    private final String nbpApiBaseUrl;

    private final boolean cacheEnabled;

    private final Map<SimpleExchangeRatesCacheKey, ExchangeRate> cache = new HashMap<>();

    @Override
    public ExchangeRate getExchangeRateFromPLN(LocalDate effectiveDate, Currency toCurrency) {
        try {
            SimpleExchangeRatesCacheKey cacheKey = new SimpleExchangeRatesCacheKey(effectiveDate, toCurrency);

            if (cacheEnabled && cache.containsKey(cacheKey)) {
                return cache.get(cacheKey);
            }

            String url = getExchangeRateUrl(toCurrency);
            NbpApiExchangeRateResponse response = restTemplate.getForObject(url, NbpApiExchangeRateResponse.class);

            BigDecimal value = response.rates().stream()
                    .map(NbpApiExchangeRateResponse.Rate::mid)
                    .findFirst()
                    .orElseThrow(() -> new ExchangeRateException("Could not find exchange rate " + toCurrency));

            ExchangeRate exchangeRate = new ExchangeRate(value);
            cache.put(cacheKey, exchangeRate);

            return exchangeRate;
        } catch (Exception e) {
            log.error("[NBP REST API] Error when getting exchange rate from NBP: ", e);
            throw new ExchangeRateException(e.getMessage());
        }
    }

    private String getExchangeRateUrl(Currency currency) {
        return nbpApiBaseUrl + String.format(NBP_API_RATE_URL, currency.name());
    }

}

record NbpApiExchangeRateResponse(String code, List<Rate> rates) {
    record Rate(BigDecimal mid) {}
}

record SimpleExchangeRatesCacheKey(LocalDate effectiveDate, Currency toCurrency) {}
