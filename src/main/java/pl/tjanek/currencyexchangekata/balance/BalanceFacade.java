package pl.tjanek.currencyexchangekata.balance;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BalanceFacade {

    private final BalanceRepository repository;

}
