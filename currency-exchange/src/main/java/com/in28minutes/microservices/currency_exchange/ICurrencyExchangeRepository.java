package com.in28minutes.microservices.currency_exchange;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {
    CurrencyExchange findByCurrencyFromAndCurrencyTo(String from, String to);
}
