package com.vti.repository;

import com.vti.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITransactionRepository extends JpaRepository<Transactions, Integer> {
}
