package com.vti.service;

import com.vti.dto.TransactionsDTO;
import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Transactions;
import com.vti.form.CreateTransactionForm;
import com.vti.form.UpdateTransactionForm;
import com.vti.models.ReportModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ITransactionService {
    Page<TransactionsDTO> filterTransactions(TransactionFilter filter, Integer userId, Pageable pageable);

    Transactions createTransaction(CreateTransactionForm createTransactionForm);

    boolean updateTransaction(Integer transactionID, UpdateTransactionForm updateTransactionForm);

    boolean deleteTransaction(List<Integer> transactionID);

    List<TransactionsDTO> getAllTransactions(Integer userID);

    Page<TransactionsDTO> getTransactions(Pageable pageable, Integer userID);

    double getAllTotalExpensesByMoneySources(Integer moneySourceID);

    double getAllTotalExpensesByTime(String type, Integer userID);

    double getAllTotalIncomesByTime(String type, Integer userID);

    List<TransactionsDTO> getRecentTransactions(Integer userID, int limit);

    Map<Integer, ReportModel> getReport(Integer userId);
}
