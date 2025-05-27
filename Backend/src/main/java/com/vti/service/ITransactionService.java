package com.vti.service;

import com.vti.dto.TransactionsDTO;
import com.vti.entity.Transactions;

public interface ITransactionService {

    Transactions createTransaction(TransactionsDTO transactionsDTO);

}
