package com.vti.controller;

import com.vti.entity.Categories;
import com.vti.repository.ICategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoriesController {
//    @Autowired
//    private ICategoriesRepository categoriesRepository;
//
//    @GetMapping
//    public List<Categories> getAllCategories() {
//        return categoriesRepository.findAll();
//    }
}
