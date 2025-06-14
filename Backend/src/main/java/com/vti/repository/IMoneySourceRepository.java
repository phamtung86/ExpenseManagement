package com.vti.repository;

import com.vti.entity.MoneySources;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMoneySourceRepository extends JpaRepository<MoneySources, Integer> {


    @EntityGraph(value = "MoneySources.full")
    List<MoneySources> findByUserId(Integer userId);

    @Query("SELECT COALESCE(SUM(m.currentBalance), 0.0) FROM money_source m WHERE m.user.id = :id")
    double getTotalCurrentBalance(@Param("id") Integer id);

}
