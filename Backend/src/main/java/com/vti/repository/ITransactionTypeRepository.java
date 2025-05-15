package com.vti.repository;

import com.vti.entity.TransactionTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITransactionTypeRepository extends JpaRepository<TransactionTypes, Integer> {
}
