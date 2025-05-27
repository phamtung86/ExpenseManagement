package com.vti.service;

import com.vti.repository.ITransactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionTypesService {

    @Autowired
    private ITransactionTypeRepository transactionTypeRepository;

}
