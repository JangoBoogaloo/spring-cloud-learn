package com.in28minutes.microservices.currency_conversion;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    private ICurrencyExchangeProxy currencyExchangeProxy;

    @GetMapping("/bulk-head-test")
    @Bulkhead(name = "default")
    public String BulkHeadTest() {
        logger.info("Request Time {} ms", System.currentTimeMillis()/1000L);
        return "Bulk head test";
    }

    @GetMapping("/rate-limit-test")
    @RateLimiter(name = "default")
    public String rateLimitTest() {
        logger.info("Request Time {} seconds", System.currentTimeMillis() / 1000L);
        return "Rate limit test";
    }

    @GetMapping("/currency-conversion/from/{currencyFrom}/to/{currencyTo}/quantity/{quantity}")
    @CircuitBreaker(name = "default", fallbackMethod = "fallbackErrorConversion")
    public CurrencyConversion calculateCurrencyConversion(
            @PathVariable String currencyFrom,
            @PathVariable String currencyTo,
            @PathVariable BigDecimal quantity) {
        logger.info("Request Time {} seconds", System.currentTimeMillis() / 1000L);
        requestCount += 1;
        if (requestCount % 5 != 0) {
            ResponseEntity<CurrencyConversion> failEntity = new RestTemplate().getForEntity("http://localhost:8100/not-found-url", CurrencyConversion.class);
            return failEntity.getBody();
        }
        requestCount = 0;
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

    private int requestCount = 0;
    private Logger logger = LoggerFactory.getLogger(CurrencyConversion.class);

    @Retry(name = "retry-5", fallbackMethod = "fallbackErrorConversion")
    @GetMapping("/currency-conversion-feign/from/{currencyFrom}/to/{currencyTo}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeign(
            @PathVariable String currencyFrom,
            @PathVariable String currencyTo,
            @PathVariable BigDecimal quantity) {
        logger.info("Request Time {} seconds", System.currentTimeMillis() / 1000L);
        requestCount += 1;
        if (requestCount % 5 != 0) {
            ResponseEntity<CurrencyConversion> failEntity = new RestTemplate().getForEntity("http://localhost:8100/not-found-url", CurrencyConversion.class);
            return failEntity.getBody();
        }
        requestCount = 0;
        CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveExchangeValue(currencyFrom, currencyTo);
        return new CurrencyConversion(
                currencyConversion.getId(),
                currencyFrom, currencyTo, quantity, currencyConversion.getRate(),
                quantity.multiply(currencyConversion.getRate()), currencyConversion.getEnvironment()+" "+"feign");
    }

    private CurrencyConversion fallbackErrorConversion(Exception e) {
        CurrencyConversion currencyConversion = new CurrencyConversion();
        currencyConversion.setError("Fail to process request");
        return currencyConversion;
    }
}
