package com.vti.service;

import com.vti.dto.CategoriesDTO;
import com.vti.entity.Categories;
import com.vti.repository.ICategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.vti.utils.ObjectUtils.convertToDto;

@Service
public class CategoriesService implements ICategoriesService{

    @Autowired
    private ICategoriesRepository categoriesRepository;
    @Override
    public Categories findById(int id) {return categoriesRepository.findById(id).orElse(null);}

    public List<CategoriesDTO> getAllCategories() {
        List<Categories> allCategories = categoriesRepository.findAll();

        List<CategoriesDTO> root = allCategories.stream()
                .filter(categories -> categories.getParentId() == null)
                .map(categories -> convertToDto(categories))
                .collect(Collectors.toList());
        for (CategoriesDTO rootDTO : root) {
            getChild(rootDTO, allCategories);
        }

        return root;
    }

    private List<CategoriesDTO> getChild(CategoriesDTO root, List<Categories> allCategories) {
//        List<Categories> categoriesCopy = allCategories.c
        List<CategoriesDTO> chils = allCategories.stream()
                .filter(categories -> categories.getParentId() != null && categories.getParentId().equals(root.getId()))
                .map(categories -> convertToDto(categories))
                .collect(Collectors.toList());
        root.setChildren(chils);
        for (CategoriesDTO child : chils) {
            getChild(child, allCategories);
        }

        return chils;
    }

    private CategoriesDTO convertToDto(Categories c) {
        return new CategoriesDTO(
                c.getId(),
                c.getName(),
                c.getParentId(),
                c.getIcon(),
                c.getTransactionTypes() != null ? String.valueOf(c.getTransactionTypes().getId()) : null,
                c.getTransactionTypes() != null ? c.getTransactionTypes().getName() : null,
                new ArrayList<>()

        );
    }

}
