package com.vti.service;

import com.vti.dto.TransactionsDTO;
import com.vti.dto.UserDTO;
import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Categories;
import com.vti.entity.MoneySources;
import com.vti.entity.Transactions;
import com.vti.form.UpdateTransactionForm;
import com.vti.repository.ITransactionRepository;
import com.vti.specification.TransactionSpecificationBuilder;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    @Autowired
    private ICategoriesService categoriesService;

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
                entity.getDescription(),
                new UserDTO(null, entity.getUser().getFullName(), null, null, null, null)
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

    @Transactional
    @Override
    public boolean updateTransaction(Integer transactionID, UpdateTransactionForm updateTransactionForm) {
        Categories categories;
        MoneySources moneySources;
        Transactions transaction = transactionRepository.findById(transactionID).orElse(null);

        if (transaction == null) {
            return false;
        }
        if (!transaction.getCategories().getId().equals(updateTransactionForm.getCategoriesId())) {
            categories = categoriesService.findById(updateTransactionForm.getCategoriesId());
            transaction.setCategories(categories);
        }
        if (!transaction.getMoneySources().getId().equals(updateTransactionForm.getMoneySourcesId())) {
            moneySources = moneySourceService.findById(updateTransactionForm.getMoneySourcesId());
            transaction.setMoneySources(moneySources);
        }
        Double oldAmount = transaction.getAmount();
        transaction.setDescription(updateTransactionForm.getDescription());
        if (!transaction.getTransactionDate().equals(updateTransactionForm.getTransactionDate())) {
            transaction.setTransactionDate(updateTransactionForm.getTransactionDate());
        }
        transaction.setUpdateAt(new Date());
        if (updateTransactionForm.getAmount() != transaction.getAmount()) {
            transaction.setAmount(updateTransactionForm.getAmount());
            moneySourceService.updateCurrentBalance(updateTransactionForm.getMoneySourcesId(), oldAmount - updateTransactionForm.getAmount());
            transaction.setAmount(updateTransactionForm.getAmount());
        }
        transactionRepository.save(transaction);
        return true;
    }

    @Override
    public boolean deleteTransaction(List<Integer> transactionID) {
        int count = 0;
        for (Integer i : transactionID) {
            transactionRepository.deleteById(i);
            count++;
        }

        return count > 0;
    }

    @Override
    public List<TransactionsDTO> getAllTransactions() {
        List<Transactions> transactions = transactionRepository.findAll();
        List<TransactionsDTO> transactionsDTOS = new ArrayList<>();
        for (Transactions transaction : transactions) {
            transactionsDTOS.add(toDTO(transaction));
        }
        return transactionsDTOS;
    }

    @Override
    public Page<TransactionsDTO> getTransactions(Pageable pageable) {
        Page<Transactions> transactions = transactionRepository.findAll(pageable);
        List<TransactionsDTO> transactionsDTOS = modelMapper.map(transactions.getContent(), new TypeToken<List<TransactionsDTO>>(){}.getType());
        Page<TransactionsDTO> transactionsDTOPage = new PageImpl<>(transactionsDTOS, pageable, transactions.getTotalElements());
        return transactionsDTOPage;
    }


}
