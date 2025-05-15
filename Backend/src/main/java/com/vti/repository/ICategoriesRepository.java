package com.vti.repository;

import com.vti.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoriesRepository extends JpaRepository<Categories, Integer> {
}
