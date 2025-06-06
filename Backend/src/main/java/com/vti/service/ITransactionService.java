package com.vti.service;

import com.vti.dto.TransactionsDTO;
import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Transactions;
import com.vti.form.CreateTransactionForm;
import com.vti.form.UpdateTransactionForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITransactionService {
    List<TransactionsDTO> filterTransactions(TransactionFilter filter);

    Transactions createTransaction(CreateTransactionForm createTransactionForm);

    boolean updateTransaction(Integer transactionID, UpdateTransactionForm updateTransactionForm);

    boolean deleteTransaction(List<Integer> transactionID);

    List<TransactionsDTO> getAllTransactions(Integer userID);

    Page<TransactionsDTO> getTransactions(Pageable pageable, Integer userID);

    double getAllTotalExpensesByMoneySources(Integer moneySourceID);

    double getAllTotalExpensesByTime(String type, Integer userID);

    double getAllTotalIncomesByTime(String type, Integer userID);

    List<TransactionsDTO> getRecentTransactions(Integer userID, int limit);
}
