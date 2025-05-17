package com.vti.repository;

import com.vti.entity.MoneySources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMoneySourceRepository extends JpaRepository<MoneySources, Integer> {
}
