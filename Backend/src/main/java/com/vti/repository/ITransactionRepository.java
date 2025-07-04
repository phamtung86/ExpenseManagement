package com.vti.repository;

import com.vti.dto.TransactionsDTO;
import com.vti.entity.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transactions, Integer>,
        JpaSpecificationExecutor<Transactions> {
    @Query("SELECT t FROM transactions t WHERE t.user.id = :userId ORDER BY t.id DESC")
    List<Transactions> findAllByUserId(@Param("userId") Integer userId);

    @Query("SELECT t FROM transactions t WHERE t.user.id = :userId ORDER BY t.id DESC")
    Page<Transactions> findAllByUserId(Pageable pageable, Integer userId);


    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM transactions t WHERE t.transactionTypes.id = 1 AND t.moneySources.id = :id AND FUNCTION('month', t.transactionDate) = :month")
    double getAllTotalExpensesByMoneySources(@Param("id") Integer id, @Param("month") int month);

    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM transactions t WHERE t.transactionTypes.id = 1 AND FUNCTION('day', t.transactionDate) = :day AND FUNCTION('month', t.transactionDate) = :month AND FUNCTION('year', t.transactionDate) = :year AND t.user.id = :userId")
    double getTotalExpenseByDay(@Param("day") int day,
                                @Param("month") int month,
                                @Param("year") int year,
                                @Param("userId") int userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM transactions t WHERE t.transactionTypes.id = 1 AND FUNCTION('month', t.transactionDate) = :month AND FUNCTION('year', t.transactionDate) = :year AND t.user.id = :userId")
    double getTotalExpenseByMonth(@Param("month") int month,
                                  @Param("year") int year,
                                  @Param("userId") int userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM transactions t WHERE t.transactionTypes.id = 1 AND FUNCTION('year', t.transactionDate) = :year AND t.user.id = :userId")
    double getTotalExpenseByYear(@Param("year") int year,
                                 @Param("userId") int userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM transactions t WHERE t.transactionTypes.id = 1 AND FUNCTION('day', t.transactionDate) = :day AND FUNCTION('month', t.transactionDate) = :month AND FUNCTION('year', t.transactionDate) = :year AND t.user.id = :userId")
    double getTotalIncomeByDay(@Param("day") int day,
                               @Param("month") int month,
                               @Param("year") int year,
                               @Param("userId") int userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM transactions t WHERE t.transactionTypes.id = 2 AND FUNCTION('month', t.transactionDate) = :month AND FUNCTION('year', t.transactionDate) = :year AND t.user.id = :userId")
    double getTotalIncomeByMonth(@Param("month") int month,
                                 @Param("year") int year,
                                 @Param("userId") int userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM transactions t WHERE t.transactionTypes.id = 2 AND FUNCTION('year', t.transactionDate) = :year AND t.user.id = :userId")
    double getTotalIncomeByYear(@Param("year") int year,
                                @Param("userId") int userId);


    @Query("SELECT t FROM transactions t WHERE t.user.id = :userId ORDER BY t.id DESC LIMIT :limit")
    List<Transactions> findRecentTransactionsByUserId(@Param("userId") Integer userId, @Param("limit") int limit);

    @Query("SELECT t FROM transactions t WHERE t.user.id = :userId AND FUNCTION('day', t.transactionDate) = :day AND FUNCTION('month', t.transactionDate) = :month AND FUNCTION('year', t.transactionDate) = :year ORDER BY t.id DESC")
    List<Transactions> getReportToday(@Param("userId") Integer userId,
                                      @Param("day") int day,
                                      @Param("month") int month,
                                      @Param("year") int year);

    @Modifying
    @Query("DELETE FROM transactions t WHERE t.id = :id")
    int hardDelete(Integer id);

}

