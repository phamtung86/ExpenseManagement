package com.vti.service;

import com.vti.dto.CategoriesDTO;
import com.vti.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ICategoriesService {

//    Categories findById(int id);
Page<CategoriesDTO> findCategories(String name, Integer parentID, Long transactionTypeId, PageRequest pageRequest);

}
