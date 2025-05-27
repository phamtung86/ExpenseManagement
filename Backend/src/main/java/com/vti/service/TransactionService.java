package com.vti.service;

import com.vti.dto.TransactionsDTO;
import com.vti.dto.UserDTO;
import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Transactions;
import com.vti.repository.ITransactionRepository;
import com.vti.specification.TransactionSpecificationBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private ITransactionRepository transactionRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IMoneySourceService moneySourceService;

    public List<TransactionsDTO> filterTransactions(TransactionFilter filter) {
        TransactionSpecificationBuilder builder = new TransactionSpecificationBuilder(filter);

        List<Transactions> entities = transactionRepository.findAll(builder.build());

        // Map từ entity sang DTO
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TransactionsDTO toDTO(Transactions entity) {
        return new TransactionsDTO(
                entity.getId(),
                entity.getAmount(),
                entity.getAction().name(),
                entity.getTransactionDate(),
                entity.getUpdateAt(),
                new UserDTO(null, entity.getUser().getFullName(), null, null, null, null, null)
                ,
                entity.getCategories().getId(),
                entity.getCategories().getName(),
                entity.getTransactionTypes().getId().toString(),
                entity.getTransactionTypes().getName(),
                entity.getTransactionTypes().getType().toString(),
                entity.getMoneySources().getId(),
                entity.getMoneySources().getName()
        );
    }

    @Transactional
    @Override
    public Transactions createTransaction(TransactionsDTO transactionsDTO) {
        Transactions transactions = modelMapper.map(transactionsDTO, Transactions.class);
        transactions.setAction(Transactions.Action.CREATED);
        transactions.setTransactionDate(new Date());
        double amount = 0;
        if (transactionsDTO.getTransactionTypeType().equals("INCOME")) {
            amount = transactionsDTO.getAmount();
        } else {
            amount = -transactionsDTO.getAmount();
        }
        moneySourceService.updateCurrentBalance(transactionsDTO.getMoneySourcesId(), amount);
        return transactionRepository.save(transactions);
    }
}
