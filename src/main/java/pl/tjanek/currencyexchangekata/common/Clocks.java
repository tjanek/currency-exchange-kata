package pl.tjanek.currencyexchangekata.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class Clocks {

    @Bean
    Clock defaultSystemClock() {
        return Clock.systemDefaultZone();
    }

}
