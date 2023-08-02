package pl.tjanek.currencyexchangekata.account;

import pl.tjanek.currencyexchangekata.common.AccountNumber;

import java.time.Instant;

record CurrencyAccount(AccountNumber number, AccountHolder holder, Instant openedAt) {}
