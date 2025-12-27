package com.in28minutes.microservices.currency_conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    private ICurrencyExchangeProxy currencyExchangeProxy;

    @GetMapping("/currency-conversion/from/{currencyFrom}/to/{currencyTo}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(
            @PathVariable String currencyFrom,
            @PathVariable String currencyTo,
            @PathVariable BigDecimal quantity) {
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("currencyFrom", currencyFrom);
        uriVariables.put("currencyTo", currencyTo);
        ResponseEntity<CurrencyConversion> responseEntity =  new RestTemplate().getForEntity(
                "http://localhost:8000/currency-exchange/from/{currencyFrom}/to/{currencyTo}",
                CurrencyConversion.class,
                uriVariables);
        CurrencyConversion currencyConversion = responseEntity.getBody();
        return new CurrencyConversion(
                currencyConversion.getId(),
                currencyFrom, currencyTo, quantity, currencyConversion.getRate(),
                quantity.multiply(currencyConversion.getRate()), currencyConversion.getEnvironment()+" "+"rest template");
    }

    @GetMapping("/currency-conversion-feign/from/{currencyFrom}/to/{currencyTo}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeign(
            @PathVariable String currencyFrom,
            @PathVariable String currencyTo,
            @PathVariable BigDecimal quantity) {
        CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveExchangeValue(currencyFrom, currencyTo);
        return new CurrencyConversion(
                currencyConversion.getId(),
                currencyFrom, currencyTo, quantity, currencyConversion.getRate(),
                quantity.multiply(currencyConversion.getRate()), currencyConversion.getEnvironment()+" "+"feign");
    }
}
