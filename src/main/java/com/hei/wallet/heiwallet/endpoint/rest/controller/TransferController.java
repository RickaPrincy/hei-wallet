package com.hei.wallet.heiwallet.endpoint.rest.controller;

import com.hei.wallet.heiwallet.endpoint.rest.mapper.TransferMapper;
import com.hei.wallet.heiwallet.endpoint.rest.model.CreateTransfer;
import com.hei.wallet.heiwallet.endpoint.rest.model.Transfer;
import com.hei.wallet.heiwallet.service.TransferService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
public class TransferController {
    private final TransferService transferService;
    private final TransferMapper transferMapper;

    @GetMapping("/transfers")
    public List<Transfer> getAll(
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to
    ){
        return transferService.findAll(from, to).stream().map(transferMapper::toRest).toList();
    }

    @PutMapping("/transfers")
    public Transfer makeTransfer(
            @RequestBody CreateTransfer transfer
    ){
        return transferMapper.toRest(
            transferService.doTransfer(transferMapper.createToDomain(transfer))
        );
    }

    public TransferController(TransferService transferService, TransferMapper transferMapper) {
        this.transferService = transferService;
        this.transferMapper = transferMapper;
    }
}
