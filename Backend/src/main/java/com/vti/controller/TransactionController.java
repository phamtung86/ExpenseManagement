package com.vti.controller;

import com.vti.dto.TransactionsDTO;
import com.vti.entity.Transactions;
import com.vti.service.ITransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody TransactionsDTO transactionsDTO) {
        Transactions transaction = transactionService.createTransaction(transactionsDTO);
        System.out.println(transaction);
        if (transaction == null) {
            return ResponseEntity.status(400).body("Create new transaction failed");
        }
        return ResponseEntity.status(200).body("Create new transaction successfully");
    }

}
