package com.vti.repository;

import com.vti.entity.TransactionTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionTypeRepository extends JpaRepository<TransactionTypes, Integer> {
}
