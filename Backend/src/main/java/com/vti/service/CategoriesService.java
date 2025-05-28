package com.vti.service;

import com.vti.entity.Categories;
import com.vti.repository.ICategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriesService implements ICategoriesService{

    @Autowired
    private ICategoriesRepository categoriesRepository;

    @Override
    public Categories findById(int id) {
        return categoriesRepository.findById(id).orElse(null);
    }
}
