![Build](https://github.com/tjanek/currency-exchange-kata/actions/workflows/build.yml/badge.svg)

# Currency Exchange Kata

### Functional requirements

 - Provide a REST API allowing for the creation of a currency account
   - when creating an account, the initial non-negative balance of the account in PLN must be provided
   - when creating an account, holder first and last name must be provided
   - generates an account identifier when creating an account, which should be used when calling further REST API calls
 - Provide a REST API for exchanging money in the PLN <-> USD pair (i.e., PLN to USD and USD to PLN), and the current exchange rate should be retrieved from the public NBP API (http://api.nbp.pl/)
 - Provide a REST API for retrieving data about the account and its current balance in PLN and USD.
