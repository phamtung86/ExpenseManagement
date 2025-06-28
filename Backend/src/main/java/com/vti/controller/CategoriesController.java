package com.vti.controller;

import com.vti.dto.CategoriesDTO;

import com.vti.form.CreateCategories;
import com.vti.form.UpdateCategories;
import com.vti.service.ICategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoriesController {


@Autowired
private ICategoriesService categoriesService;

//public CategoriesController(ICategoriesService categoriesService) {
//    this.categoriesService = categoriesService;
//}

    /**
     * FLOW:
     * [1] Nhận userID từ path param.
     * [2] Gọi Service lấy tất cả Categories của user.
     * [3] Service gọi Repository query DB.
     * [4] Trả List<CategoriesDTO>.
     */
@GetMapping("user/{id}")
public ResponseEntity<List<CategoriesDTO>> getAllCategories(@PathVariable(name = "id") int userID) {
    return new ResponseEntity<>(categoriesService.getAllCategories(userID), HttpStatus.OK);
}

    /**
     * FLOW:
     * [1] Nhận userID từ path param.
     * [2] Gọi Service lấy tất cả Categories của user.
     * [3] Service lọc root (parentId == null).
     * [4] Duyệt đệ quy gán children.
     * [5] Trả List<CategoriesDTO> dạng cây.
     */
    @GetMapping("/parent-child/user/{id}")
    public List<CategoriesDTO> getAllCategoriesWithParentChild(@PathVariable(name = "id") int userID) {
        return categoriesService.getAllCategoriesWithParentChild(userID);
    }
    @PostMapping
    public ResponseEntity<?> createCategories(@RequestBody CreateCategories categories) {
        categoriesService.createCategories(categories);
        return new ResponseEntity<>("Created category successfully", HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategories(@PathVariable int id,@RequestBody UpdateCategories updateCategories) {
        boolean isUpdated = categoriesService.updateCategories(updateCategories, id);

        if (isUpdated) {
            return ResponseEntity.ok("Category updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Category not found with id = " + id);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategories(@PathVariable int id) {
        boolean isDeleted = categoriesService.deleteCategories(id);
        if (isDeleted) {
            return ResponseEntity.ok("Category deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Category not found with id = " + id);
        }


    }
}
