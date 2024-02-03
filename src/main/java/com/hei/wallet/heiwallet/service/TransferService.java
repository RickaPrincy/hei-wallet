package com.hei.wallet.heiwallet.service;

import com.hei.wallet.heiwallet.exception.BadRequestException;
import com.hei.wallet.heiwallet.exception.InternalServerErrorException;
import com.hei.wallet.heiwallet.model.Transaction;
import com.hei.wallet.heiwallet.model.TransactionType;
import com.hei.wallet.heiwallet.model.Transfer;
import com.hei.wallet.heiwallet.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final static String TRANSFER_LABEL="TRANSFER";
    private final static String CATEGORY_ID_FOR_TRANSFER="category_transfer";

    public Transfer doTransfer(Transfer transfer){
        if(transfer.getDestination().getId().equals(transfer.getSource().getId())){
            throw new BadRequestException("Cannot make a transfer to the account's it self");
        }
        final Instant CURRENT_DATETIME = Instant.now();
        Transaction sourceTransaction = new Transaction(
            UUID.randomUUID().toString(),
            TRANSFER_LABEL,
            transfer.getAmount().negate(),
            CURRENT_DATETIME,
            TransactionType.DEBIT,
            categoryService.findById(CATEGORY_ID_FOR_TRANSFER),
            transfer.getSource()
        );

        Transaction destinationTransaction = new Transaction(
            UUID.randomUUID().toString(),
            TRANSFER_LABEL,
            transfer.getAmount(),
            CURRENT_DATETIME,
            TransactionType.CREDIT,
            categoryService.findById(CATEGORY_ID_FOR_TRANSFER),
            transfer.getDestination()
        );

        transactionService.doTransaction(sourceTransaction);
        transactionService.doTransaction(destinationTransaction);
        saveOrUpdateAll(List.of(transfer));
        return findById(transfer.getId());
    }
    public List<Transfer> findAll(Instant from, Instant to) {
        try {
            if(from == null || to == null){
                return transferRepository.findAll();
            }
            if(from.isAfter(to))
                throw new BadRequestException("from must be before to");
            if(to.isAfter(Instant.now()))
                throw new BadRequestException("to cannot be in the future");
            return transferRepository.findTransferInInterval(from, to);
        } catch (SQLException error) {
            throw new InternalServerErrorException(error.getMessage());
        }
    }

    public Transfer findById(String transferId) {
        try {
            return transferRepository.findById(transferId);
        } catch (SQLException error) {
            throw new InternalServerErrorException(error.getMessage());
        }
    }

    public List<Transfer> saveOrUpdateAll(List<Transfer> transfer) {
        try {
            return transferRepository.saveOrUpdateAll(transfer);
        } catch (SQLException error) {
            throw new InternalServerErrorException(error.getMessage());
        }
    }

    public TransferService(TransferRepository transferRepository, TransactionService transactionService, CategoryService categoryService) {
        this.transferRepository = transferRepository;
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

}
