package com.vti.repository;

import com.vti.entity.MoneySources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMoneySourceRepository extends JpaRepository<MoneySources, Integer> {
}
