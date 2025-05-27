package com.vti.service;

import com.vti.repository.ICategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriesService implements ICategoriesService{

    @Autowired
    private ICategoriesRepository categoriesRepository;
}
