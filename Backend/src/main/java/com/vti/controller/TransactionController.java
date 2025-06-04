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

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TransactionsDTO>> getAllTransactions(@PathVariable(name = "id") int userId) {
        List<TransactionsDTO> transactionsDTO = transactionService.getAllTransactions(userId);
        return ResponseEntity.ok(transactionsDTO);
    }

    @GetMapping("/page/user/{id}")
    public ResponseEntity<Page<TransactionsDTO>> getAllPagesTransactions(Pageable pageable, @PathVariable(name = "id") int userId) {
        return ResponseEntity.ok(transactionService.getTransactions(pageable, userId));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TransactionsDTO>> getFilteredTransactions(@ModelAttribute TransactionFilter filter) {
        return ResponseEntity.ok(transactionService.filterTransactions(filter));
    }

    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody CreateTransactionForm createTransactionForm) {
        Transactions transaction = transactionService.createTransaction(createTransactionForm);
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

    @GetMapping("/total-expense/user/{userID}")
    public ResponseEntity<Double> getTotalExpense(@RequestParam(name = "type") String type, @PathVariable("userID") Integer userId) {
        return ResponseEntity.status(200).body(transactionService.getAllTotalExpensesByTime(type,userId));
    }

    @GetMapping("/total-income/user/{userID}")
    public ResponseEntity<Double> getTotalIncome(@RequestParam(name = "type") String type, @PathVariable("userID") Integer userId) {
        return ResponseEntity.status(200).body(transactionService.getAllTotalIncomesByTime(type,userId));
    }

    @GetMapping("/recent-transactions/user/{userId}/limit/{limit}")
    public ResponseEntity<List<TransactionsDTO>> getRecentTransactions(@PathVariable(name = "userId") Integer userId, @PathVariable(name = "limit") int limit) {
        return ResponseEntity.status(200).body(transactionService.getRecentTransactions(userId, limit));
    }
}
