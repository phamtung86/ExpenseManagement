package com.vti.controller;

import com.vti.dto.TransactionsDTO;
import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Transactions;
import com.vti.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @GetMapping("/filter")
    public List<TransactionsDTO> getFilteredTransactions(@ModelAttribute TransactionFilter filter) {
        return transactionService.filterTransactions(filter);
    }

}

