package com.hei.wallet.heiwallet.endpoint.rest.controller;

import com.hei.wallet.heiwallet.endpoint.rest.mapper.CurrencyValueMapper;
import com.hei.wallet.heiwallet.endpoint.rest.model.CreateCurrencyValue;
import com.hei.wallet.heiwallet.endpoint.rest.model.CurrencyValue;
import com.hei.wallet.heiwallet.service.CurrencyValueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CurrencyValueController {
    private final CurrencyValueService currencyValueService;
    private final CurrencyValueMapper currencyValueMapper;

    @GetMapping("/currency/{currencyId}/values")
    public List<CurrencyValue> getAllByCurrencyId(@PathVariable String currencyId){
        return currencyValueService
                .findAllByCurrencyId(currencyId)
                .stream().map(currencyValueMapper::toRest)
                .toList();
    }

    @GetMapping("/currency/{currencyId}/values/current")
    public CurrencyValue getCurrentCurrencyValue(@PathVariable String currencyId){
        return currencyValueMapper.toRest(
            currencyValueService
                .findCurrencyCurrentValue(currencyId)
                .orElse(null)
        );
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
