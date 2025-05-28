package com.vti.service;

import com.vti.dto.TransactionsDTO;
import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Transactions;
import com.vti.form.UpdateTransactionForm;

import java.util.List;

public interface ITransactionService {
    public List<TransactionsDTO> filterTransactions(TransactionFilter filter);

    Transactions createTransaction(TransactionsDTO transactionsDTO);

    boolean updateTransaction(Integer transactionID,UpdateTransactionForm updateTransactionForm);

    boolean deleteTransaction(List<Integer> transactionID);

}
