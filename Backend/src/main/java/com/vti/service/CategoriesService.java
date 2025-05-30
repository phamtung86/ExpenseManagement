package com.vti.service;

import com.vti.entity.Categories;
import com.vti.repository.ICategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CategoriesService implements ICategoriesService{

    @Autowired
    private ICategoriesRepository categoriesRepository;

    @Override
    public Categories findById(int id) {
        return categoriesRepository.findById(id).orElse(null);
    }

    //    @Override
//    public List<Categories> findAllCategories() {
//        List<Categories> allCategories = categoriesRepository.findAll();
//
//        // Tìm id các danh mục cha (parentId == null)
//        List<Integer> parentIds = new ArrayList<>();
//        for (Categories c : allCategories) {
//            if (c.getParentId() == null) {
//                parentIds.add(c.getId());
//            }
//        }
//        // Fix parentId của các danh mục con không hợp lệ
//        for (Categories c : allCategories) {
//            if(c.getParentId() != null && !parentIds.contains(c.getParentId())) {
//                // Nếu parentId không nằm trong danh sách cha, fix thành null (biến thành cha)
//                c.setParentId(null);
//            }
//        }
//        return allCategories;
//    }
}
