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

@RestController
@RequestMapping("api/v1/categories")
public class CategoriesController {


    @Autowired
    private ICategoriesService categoriesService;

    @GetMapping
    public ResponseEntity<List<CategoriesDTO>> getAllCategories() {
        return new ResponseEntity<>(categoriesService.getAllCategories(), HttpStatus.OK);
    }

    @GetMapping("/parent-child")
    public List<CategoriesDTO> getAllCategoriesWithParentChild() {
        return categoriesService.getAllCategories();
    }

    @PostMapping
    public ResponseEntity<?> createCategories(@RequestBody CreateCategories categories) {
        categoriesService.createCategories(categories);
        return new ResponseEntity<>("Created category successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategories(@PathVariable int id, @RequestBody UpdateCategories updateCategories) {
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
