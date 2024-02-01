package com.hei.wallet.heiwallet.endpoint.rest.controller;

import com.hei.wallet.heiwallet.model.Balance;
import com.hei.wallet.heiwallet.service.BalanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BalanceController {
    private final BalanceService balanceService;

    @GetMapping("/balances")
    public List<Balance> getAllBalances(){
        return balanceService.getAll();
    }

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }
}
