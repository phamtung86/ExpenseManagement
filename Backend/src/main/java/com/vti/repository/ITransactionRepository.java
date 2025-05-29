package com.vti.repository;

import com.vti.entity.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transactions, Integer>,
        JpaSpecificationExecutor<Transactions> {
    List<Transactions> findAllByUserId(Integer userId);

    Page<Transactions> findAllByUserId(Pageable pageable, Integer userId);

    @Query("SELECT SUM(t.amount) FROM transactions t WHERE t.transactionTypes.id = 2 AND t.moneySources.id = :id")
    Double getAllTotalExpensesByMoneySources(@Param("id") Integer id);


}

