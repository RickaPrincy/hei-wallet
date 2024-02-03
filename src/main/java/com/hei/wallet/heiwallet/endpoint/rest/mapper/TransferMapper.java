package com.hei.wallet.heiwallet.endpoint.rest.mapper;

import com.hei.wallet.heiwallet.endpoint.rest.model.*;
import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.exception.NotFoundException;
import com.hei.wallet.heiwallet.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.Instant;

@Component
public class TransferMapper {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public TransferMapper(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public com.hei.wallet.heiwallet.model.Transfer createToDomain(CreateTransfer transfer){
        try{
            final com.hei.wallet.heiwallet.model.Account source = accountRepository.findById(transfer.getSourceId());
            final com.hei.wallet.heiwallet.model.Account destination = accountRepository.findById(transfer.getDestinationId());

            if(source == null || destination == null){
                throw new NotFoundException("Destination or source account are not found");
            }

            return new com.hei.wallet.heiwallet.model.Transfer(
                    transfer.getId(),
                    transfer.getAmount(),
                    Instant.now(),
                    source,
                    destination
            );
        }catch(SQLException error){
            throw new InternalServerErrorException();
        }
    }

    public Transfer toRest(com.hei.wallet.heiwallet.model.Transfer transfer){
        if(transfer == null)
            return null;
        return new Transfer(
                transfer.getId(),
                transfer.getAmount(),
                Instant.now(),
                accountMapper.toRest(transfer.getSource()),
                accountMapper.toRest(transfer.getDestination())
        );
    }
}
