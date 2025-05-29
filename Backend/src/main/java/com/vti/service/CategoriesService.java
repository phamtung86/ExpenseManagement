package com.vti.service;

import com.vti.dto.CategoriesDTO;
import com.vti.entity.Categories;
import com.vti.repository.ICategoriesRepository;
import com.vti.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CategoriesService implements ICategoriesService{

    @Autowired
    private ICategoriesRepository categoriesRepository;

    @Override
    public Page<CategoriesDTO> findCategories(String name, Integer parentId, Long transactionTypeId, PageRequest pageRequest) {
        Page<Categories> categoriesPage;

        if (name != null && !name.isEmpty()) {
            categoriesPage = categoriesRepository.findByNameContainingIgnoreCase(name, pageRequest);
        } else if (parentId != null) {
            categoriesPage = categoriesRepository.findByParentId(parentId, pageRequest);
        } else if (transactionTypeId != null) {
            categoriesPage = categoriesRepository.findByTransactionTypesId(transactionTypeId, pageRequest);
        } else {
            categoriesPage = categoriesRepository.findAll(pageRequest);
        }

        return categoriesPage.map(ObjectUtils::convertToDto);
    }
}
