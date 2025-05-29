package com.vti.controller;



import com.vti.dto.TransactionsDTO;
import com.vti.dto.filter.TransactionFilter;
import com.vti.entity.Transactions;
import com.vti.form.CreateTransactionForm;
import com.vti.form.UpdateTransactionForm;
import com.vti.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionsDTO>> getAllTransactions() {
        List<TransactionsDTO> transactionsDTO = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactionsDTO);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<TransactionsDTO>> getAllTransactions(Pageable pageable) {
        return ResponseEntity.ok(transactionService.getTransactions(pageable));
    }

    @GetMapping("/filter")
    public List<TransactionsDTO> getFilteredTransactions(@ModelAttribute TransactionFilter filter) {
        return transactionService.filterTransactions(filter);
    }

    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody CreateTransactionForm createTransactionForm) {
        Transactions transaction = transactionService.createTransaction(createTransactionForm);
        System.out.println(transaction);
        if (transaction == null) {
            return ResponseEntity.status(400).body("Create new transaction failed");
        }
        return ResponseEntity.status(200).body("Create new transaction successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable int id, @RequestBody UpdateTransactionForm updateTransactionForm) {
        boolean isUpdate = transactionService.updateTransaction(id, updateTransactionForm);
        if (isUpdate) {
            return ResponseEntity.status(200).body("Update transaction successfully");
        }
        return ResponseEntity.status(400).body("Update transaction failed");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTransaction(@RequestBody List<Integer> ids) {
        boolean isDelete = transactionService.deleteTransaction(ids);
        if (isDelete) {
            return ResponseEntity.status(200).body("Delete transaction successfully");
        }
        return ResponseEntity.status(400).body("Delete transaction failed");
    }

}
