package com.in28minutes.microservices.currency_exchange;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class CurrencyExchange {

    @Id
    private Long id;

    @Column(name = "currency_from")
    private String currencyFrom;

    @Column(name = "currency_to")
    private String currencyTo;

    private BigDecimal rate;
    private String environment;

    public CurrencyExchange() {
        super();
    }
    public CurrencyExchange(Long id, String currencyFrom, String currencyTo, BigDecimal rate, String environment) {
        super();
        this.id = id;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.rate = rate;
        this.environment = environment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String from) {
        this.currencyFrom = from;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String to) {
        this.currencyTo = to;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
