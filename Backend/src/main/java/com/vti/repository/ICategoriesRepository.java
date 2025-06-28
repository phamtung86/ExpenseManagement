package com.vti.repository;

import com.vti.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoriesRepository extends JpaRepository<Categories, Integer> {

    /**
     * FLOW:
     * Lấy tất cả danh mục cho userID.
     * Bao gồm: global categories (user IS NULL) + categories riêng của user.
     */
    @Query("SELECT c FROM categories c WHERE c.user IS NULL OR c.user.id = :userID")
    List<Categories> findAllCategoriesByUserId(@Param("userID") Integer userId);

    /**
     * FLOW:
     * Kiểm tra 1 category có phải danh mục cha hay không.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM categories c WHERE c.id = :id AND c.parentId IS NULL")
    boolean isParentCategories(@Param("id") Integer id);
}
