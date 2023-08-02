package pl.tjanek.currencyexchangekata

import groovy.json.JsonOutput
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import pl.tjanek.currencyexchangekata.abilities.HttpRequestAbility
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

import static pl.tjanek.currencyexchangekata.BaseIntegrationSpec.NOW

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(BaseTestConfiguration)
class BaseIntegrationSpec extends Specification implements HttpRequestAbility {

    static final Instant NOW = Instant.parse("2023-08-02T17:30:00.00Z")

    @Value('${local.server.port}')
    private int port

    @Autowired
    private TestRestTemplate restTemplate

    @PostConstruct
    void init() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory())
    }

    ResponseEntity<Map> request(String url, HttpMethod method, Map body = null) {
        def httpHeaders = new HttpHeaders()
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json")
        httpHeaders.add(HttpHeaders.ACCEPT, "application/json")
        restTemplate.exchange(
                "http://localhost:$port$url",
                method,
                new HttpEntity<>(JsonOutput.toJson(body), httpHeaders),
                Map)
    }

}

@TestConfiguration
class BaseTestConfiguration {

    @Bean
    @Primary
    Clock testClock() {
        return Clock.fixed(NOW, ZoneId.systemDefault())
    }

}
