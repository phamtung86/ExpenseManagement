package com.vti.service;

import com.vti.dto.TransactionsDTO;
import com.vti.dto.UserDTO;
import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Transactions;
import com.vti.repository.ITransactionRepository;
import com.vti.specification.TransactionSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService {
    @Autowired
    private ITransactionRepository transactionRepository;

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



}
