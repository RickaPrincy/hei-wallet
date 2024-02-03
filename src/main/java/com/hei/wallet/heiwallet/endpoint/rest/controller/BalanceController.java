package com.hei.wallet.heiwallet.endpoint.rest.controller;

import com.hei.wallet.heiwallet.endpoint.rest.mapper.BalanceMapper;
import com.hei.wallet.heiwallet.endpoint.rest.model.Balance;
import com.hei.wallet.heiwallet.exception.NotFoundException;
import com.hei.wallet.heiwallet.service.BalanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BalanceController {
    private final BalanceService balanceService;
    private final BalanceMapper balanceMapper;

    @GetMapping("/accounts/{accountId}/balances")
    public List<Balance> getAllByAccountId(@PathVariable String accountId){
        return balanceService
                .findAllByAccountId(accountId)
                .stream()
                .map(balanceMapper::toRest).toList();
    }

    @GetMapping("/accounts/{accountId}/balances/current")
    public Balance getAccountCurrentBalance(@PathVariable String accountId){
        return balanceMapper.toRest(
                balanceService.findCurrentBalanceByAccountId(accountId)
                .orElseThrow(NotFoundException::new)
        );
    }

    public BalanceController(BalanceService balanceService, BalanceMapper balanceMapper) {
        this.balanceService = balanceService;
        this.balanceMapper = balanceMapper;
    }
}
