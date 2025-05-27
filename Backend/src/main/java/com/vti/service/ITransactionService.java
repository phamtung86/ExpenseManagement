package com.vti.service;

import com.vti.dto.TransactionsDTO;
import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Transactions;

import java.util.List;

public interface ITransactionService {
    public List<TransactionsDTO> filterTransactions(TransactionFilter filter);

    Transactions createTransaction(TransactionsDTO transactionsDTO);

}
