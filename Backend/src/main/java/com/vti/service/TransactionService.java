package com.vti.service;

import com.vti.dto.TransactionsDTO;
import com.vti.entity.Transactions;
import com.vti.repository.ITransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TransactionService implements ITransactionService{

    @Autowired
    private ITransactionRepository transactionRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IMoneySourceService moneySourceService;

    @Transactional
    @Override
    public Transactions createTransaction(TransactionsDTO transactionsDTO) {
        Transactions transactions = modelMapper.map(transactionsDTO, Transactions.class);
        transactions.setAction(Transactions.Action.CREATED);
        transactions.setTransactionDate(new Date());
        double amount = 0;
        if(transactionsDTO.getTransactionTypeType().equals("INCOME")){
            amount = transactionsDTO.getAmount();
        } else {
            amount = -transactionsDTO.getAmount();
        }
        moneySourceService.updateCurrentBalance(transactionsDTO.getMoneySourcesId(), amount);
        return transactionRepository.save(transactions);
    }
}
