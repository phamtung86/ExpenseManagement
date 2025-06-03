package com.vti.controller;

import com.vti.dto.CategoriesDTO;
import com.vti.service.ICategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoriesController {
    @Autowired
    private ICategoriesService categoryService;

    public CategoriesController(ICategoriesService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoriesDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
