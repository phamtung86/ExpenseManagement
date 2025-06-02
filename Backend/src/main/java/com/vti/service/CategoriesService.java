package com.vti.service;

import com.vti.entity.Categories;
import com.vti.form.CreateCategories;
import com.vti.form.UpdateCategories;
import com.vti.repository.ICategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriesService implements ICategoriesService{

    @Autowired
    private ICategoriesRepository categoriesRepository;

    @Override
    public void createCategories(CreateCategories create) {
        Categories categories = new Categories();
            categories.setName(create.getName());
            categories.setParentId(create.getParentId());
            categories.setIcon(create.getIcon());
            categoriesRepository.save(categories);
    }

    @Override
    public boolean updateCategories(UpdateCategories update,Integer id) {
        Categories categories = findById(id);
        if (categories==null){
            return false;
        }
        categories.setName(update.getName());
        categories.setParentId(update.getParentId());
        categories.setIcon(update.getIcon());
        categoriesRepository.save(categories);
        return true;
    }

    @Override
    public boolean deleteCategories(Integer id) {
        Optional<Categories> categories = categoriesRepository.findById(id);
        if (categories.isEmpty()){
            return false;
        }
        categoriesRepository.deleteById(id);
        return true;
    }

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
