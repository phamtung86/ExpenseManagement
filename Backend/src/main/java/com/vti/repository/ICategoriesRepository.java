package com.vti.repository;

import com.vti.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriesRepository extends JpaRepository<Categories, Integer> {
//    Page<Categories> findByNameContainingIgnoreCase(String name, Pageable pageable);
//    Page<Categories> findByParentId(Integer parentId, Pageable pageable);
//    Page<Categories> findByTransactionTypesId(Long transactionTypeId, Pageable pageable);
}
