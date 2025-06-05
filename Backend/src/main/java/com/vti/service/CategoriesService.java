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


@Service
public class CategoriesService implements ICategoriesService {


    @Autowired
    private ICategoriesRepository categoriesRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public void createCategories(CreateCategories create) {
//        Categories categories = new Categories();
//        categories.setName(create.getName());
//        categories.setParentId(create.getParentId());
//        categories.setIcon(create.getIcon());
        Categories categories = modelMapper.map(create, Categories.class);
        categories.setId(null);

        categoriesRepository.save(categories);
    }

    @Override
    public boolean updateCategories(UpdateCategories update, Integer id) {
        Categories categories = findById(id);
        if (categories == null) {
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
        if (categories.isEmpty()) {
            return false;
        }
        categoriesRepository.deleteById(id);
        return true;
    }

    @Override
    public Categories findById(int id) {
        return categoriesRepository.findById(id).orElse(null);
    }

    public List<CategoriesDTO> getAllCategoriesWithParentChild() {
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

    @Override
    public List<CategoriesDTO> getAllCategories() {
        List<Categories> allCategories = categoriesRepository.findAll();
        List<CategoriesDTO> categoriesDTOS = modelMapper.map(allCategories, new TypeToken<List<CategoriesDTO>>() {}.getType());
        return categoriesDTOS;
    }

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
