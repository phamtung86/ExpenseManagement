package com.vti.service;

import com.vti.dto.CategoriesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ICategoriesService {
    Page<CategoriesDTO> findCategories(String name, Integer parentId, Long transactionTypeId, PageRequest pageRequest);
}
