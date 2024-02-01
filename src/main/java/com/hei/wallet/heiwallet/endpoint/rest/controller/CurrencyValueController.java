package com.hei.wallet.heiwallet.endpoint.rest.controller;

import com.hei.wallet.heiwallet.endpoint.rest.mapper.CurrencyValueMapper;
import com.hei.wallet.heiwallet.endpoint.rest.model.CreateCurrencyValue;
import com.hei.wallet.heiwallet.endpoint.rest.model.CurrencyValue;
import com.hei.wallet.heiwallet.service.CurrencyValueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CurrencyValueController {
    private final CurrencyValueService currencyValueService;
    private final CurrencyValueMapper currencyValueMapper;

    @GetMapping("/currency/values")
    public List<CurrencyValue> getAll(){
        return currencyValueService
                .getAll()
                .stream()
                .map(currencyValueMapper::toRest).toList();
    }

    @PutMapping("/currency/values")
    public List<CurrencyValue> crupdateAll(@RequestBody List<CreateCurrencyValue> categories){
        return currencyValueService.saveOrUpdateAll(
                categories.stream().map(currencyValueMapper::createToDomain).toList()
        ).stream().map(currencyValueMapper::toRest).toList();
    }

    public CurrencyValueController(CurrencyValueService currencyValueService, CurrencyValueMapper currencyValueMapper) {
        this.currencyValueService = currencyValueService;
        this.currencyValueMapper = currencyValueMapper;
    }
}
