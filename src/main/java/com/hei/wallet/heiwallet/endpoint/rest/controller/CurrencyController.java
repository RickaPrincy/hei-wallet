package com.hei.wallet.heiwallet.endpoint.rest.controller;

import com.hei.wallet.heiwallet.endpoint.rest.mapper.CurrencyMapper;
import com.hei.wallet.heiwallet.endpoint.rest.model.Currency;
import com.hei.wallet.heiwallet.service.CurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CurrencyController {
    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    @GetMapping("/currency")
    public List<Currency> getAll(){
        return currencyService
                .getAll()
                .stream()
                .map(currencyMapper::toRest).toList();
    }

    @PutMapping("/currency")
    public List<Currency> crupdateAll(@RequestBody List<Currency> categories){
        return currencyService.saveOrUpdateAll(
                categories.stream().map(currencyMapper::toDomain).toList()
        ).stream().map(currencyMapper::toRest).toList();
    }

    public CurrencyController(CurrencyService currencyService, CurrencyMapper currencyMapper) {
        this.currencyService = currencyService;
        this.currencyMapper = currencyMapper;
    }
}
