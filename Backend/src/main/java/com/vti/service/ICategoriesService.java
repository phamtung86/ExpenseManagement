package com.vti.service;

import com.vti.dto.CategoriesDTO;
import com.vti.entity.Categories;

import java.util.List;

public interface ICategoriesService {

    Categories findById(int id);
//    List<CategoriesDTO> getAllCategories();
}
