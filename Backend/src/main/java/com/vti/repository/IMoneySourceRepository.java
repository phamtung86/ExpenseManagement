package com.vti.repository;

import com.vti.entity.MoneySources;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMoneySourceRepository extends JpaRepository<MoneySources, Integer> {

    @EntityGraph(value = "MoneySources.full")
    List<MoneySources> findAll();

}
