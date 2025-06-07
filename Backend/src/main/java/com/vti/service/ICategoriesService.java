package com.vti.service;

import com.vti.dto.CategoriesDTO;
import com.vti.entity.Categories;
import com.vti.form.CreateCategories;
import com.vti.form.UpdateCategories;

import java.util.List;

public interface ICategoriesService {

    void createCategories(CreateCategories create);
    boolean updateCategories(UpdateCategories update,Integer id);
    boolean deleteCategories(Integer id);
    Categories findById(int id);

   List<CategoriesDTO> getAllCategoriesWithParentChild(Integer userID);

   List<CategoriesDTO> getAllCategories(Integer userID);
}
