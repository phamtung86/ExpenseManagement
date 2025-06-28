package com.vti.service;

import com.vti.dto.CategoriesDTO;
import com.vti.entity.Categories;

import com.vti.form.CreateCategories;
import com.vti.form.UpdateCategories;

import com.vti.repository.ICategoriesRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import java.util.*;
import java.util.stream.Collectors;

import static com.vti.utils.ObjectUtils.convertToDto;


@Service
public class CategoriesService implements ICategoriesService{

    @Autowired
    private ICategoriesRepository categoriesRepository;
    @Autowired
    private ModelMapper modelMapper;


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

    /**
     * FLOW: API hiển thị danh mục dạng cây cha-con.
     * 1. Query tất cả Categories theo userID (cả global + user riêng).
     * 2. Lọc ra các root Categories (parentId == null).
     * 3. Map Entity -> DTO.
     * 4. Đệ quy getChild() để gán các Categories con.
     * 5. Trả List<CategoriesDTO> dạng cây.
     */
    public List<CategoriesDTO> getAllCategoriesWithParentChild(Integer userID) {
        List<Categories> allCategories = categoriesRepository.findAllCategoriesByUserId(userID);

        List<CategoriesDTO> root = allCategories.stream()
                .filter(categories -> categories.getParentId() == null)
                .map(categories -> convertToDto(categories))
                .collect(Collectors.toList());
        for (CategoriesDTO rootDTO : root) {
            getChild(rootDTO, allCategories);
        }

        return root;
    }

    /**
     * FLOW: API hiển thị tất cả danh mục (phẳng).
     * 1. Query tất cả Categories theo userID (cả global + user riêng).
     * 2. Map Entity -> DTO.
     * 3. Trả List<CategoriesDTO>.
     */
    @Override
    public List<CategoriesDTO> getAllCategories(Integer userID) {
        List<Categories> allCategories = categoriesRepository.findAllCategoriesByUserId(userID);
        List<CategoriesDTO> categoriesDTOS = modelMapper.map(allCategories, new TypeToken<List<CategoriesDTO>>() {
        }.getType());
        return categoriesDTOS;
    }

    @Override
    public boolean isParentCategories(Integer id) {
        return categoriesRepository.isParentCategories(id);
    }

    /**
     * FLOW: API hiển thị danh mục theo TransactionType.
     * 1. Query Categories theo transactionTypeId + userID.
     * 2. Map Entity -> DTO.
     * 3. Trả List<CategoriesDTO>.
     */
    @Override
    public List<CategoriesDTO> getAllCategoriesByTransactionType(Integer transactionTypeId,Integer userID) {
        List<Categories> categories = categoriesRepository.findAllCategoriesByUserIdAndTransactionTypesId(transactionTypeId, userID);

        return modelMapper.map(categories, new TypeToken<List<CategoriesDTO>>(){}.getType());
    }

    /**
     * Đệ quy tìm và gán Children cho 1 Category cha.
     * FLOW:
     * 1. Lọc ra Categories con có parentId = id cha.
     * 2. Map sang DTO.
     * 3. Gán vào field children.
     * 4. Tiếp tục đệ quy với các con.
     */
    private List<CategoriesDTO> getChild(CategoriesDTO root, List<Categories> allCategories) {
//        List<Categories> categoriesCopy = allCategories.c
        List<CategoriesDTO> chils = allCategories.stream()
                .filter(categories -> categories.getParentId() != null && categories.getParentId().equals(root.getId()))
                .map(categories -> convertToDto(categories))
                .collect(Collectors.toList());

        root.setChildren((chils));
        for (CategoriesDTO child : chils) {
            getChild(child, allCategories);
        }

        root.setChildren(chils);
        for (CategoriesDTO child : chils) {
            getChild(child, allCategories);
        }
        return chils;
    }

    //    private CategoriesDTO convertToDto(Categories c) {
//        return new CategoriesDTO(
//                c.getId(),
//                c.getName(),
//                c.getParentId(),
//                c.getIcon(),
//                c.getTransactionTypes() != null ? String.valueOf(c.getTransactionTypes().getId()) : null,
//                c.getTransactionTypes() != null ? c.getTransactionTypes().getName() : null,
//                new ArrayList<>(),
//                c.getTransactions()
//
//        );
//    }

    /**
     * Convert Entity -> DTO.
     */
    public CategoriesDTO convertToDto(Categories category) {
        return modelMapper.map(category, CategoriesDTO.class);
    }
    }


}
