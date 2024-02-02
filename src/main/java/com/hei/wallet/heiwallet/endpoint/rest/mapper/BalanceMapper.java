package com.hei.wallet.heiwallet.endpoint.rest.mapper;

import com.hei.wallet.heiwallet.endpoint.rest.model.Balance;
import org.springframework.stereotype.Component;

@Component
public class BalanceMapper {

    public Balance toRest(com.hei.wallet.heiwallet.model.Balance balance){
        if(balance == null)
            return null;
        return new Balance(
            balance.getId(),
            balance.getCreationDatetime(),
            balance.getAmount()
        );
    }
}
