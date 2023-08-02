package pl.tjanek.currencyexchangekata.assertions

import java.util.regex.Pattern

class CommonAssertion {

    static final Pattern UUID_PATTERN = ~/^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/

    def assertThatIsUUID(String uuid) {
        assert uuid ==~ UUID_PATTERN
    }

}
