package com.vti.controller;

import com.vti.dto.CategoriesDTO;
import com.vti.service.ICategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/categories")
public class CategoriesController {
    @Autowired
    private ICategoriesService categoryService;

    @RequestMapping("/find")
    public Page<CategoriesDTO> findCategories(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "parentId", required = false) Integer parentId,
            @RequestParam(value = "transactionTypeId", required = false) Long transactionTypeId,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        int page = (pageNum == null || pageNum <= 0) ? 0 : pageNum;
        int size = (pageSize == null || pageSize <= 0) ? 10 : pageSize;
        PageRequest request = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        return categoryService.findCategories(name, parentId, transactionTypeId, request);
    }
}
