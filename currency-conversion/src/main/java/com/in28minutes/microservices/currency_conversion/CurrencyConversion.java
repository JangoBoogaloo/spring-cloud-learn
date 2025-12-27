package com.in28minutes.microservices.currency_conversion;

import java.math.BigDecimal;

public class CurrencyConversion {

    private Long id;

    private String currencyFrom;

    private String currencyTo;

    private BigDecimal rate;

    private BigDecimal quantity;

    private BigDecimal totalCalculatedAmount;

    private String environment;

    public CurrencyConversion() {
        super();
    }

    public CurrencyConversion(Long id, String currencyFrom, String currencyTo, BigDecimal quantity, BigDecimal rate, BigDecimal totalCalculatedAmount, String environment) {
        super();
        this.id = id;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.rate = rate;
        this.quantity = quantity;
        this.totalCalculatedAmount = totalCalculatedAmount;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalCalculatedAmount() {
        return totalCalculatedAmount;
    }

    public void setTotalCalculatedAmount(BigDecimal totalCalculatedAmount) {
        this.totalCalculatedAmount = totalCalculatedAmount;
    }
}
