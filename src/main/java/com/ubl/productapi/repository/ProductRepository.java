package com.ubl.productapi.repository;

import com.ubl.productapi.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // ============================================================
    // 1. JPQL QUERY (search by name)
    // ============================================================
    @Query("""
           SELECT p FROM Product p 
           WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    Page<Product> searchByName(@Param("keyword") String keyword, Pageable pageable);


    // ============================================================
    // 2. JPQL QUERY (search products by category name)
    // ============================================================
    @Query("""
           SELECT p FROM Product p 
           WHERE LOWER(p.category.name) = LOWER(:categoryName)
           """)
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);


    // ============================================================
    // 3. NATIVE QUERY (menggunakan category_id)
    // ============================================================
    @Query(value = "SELECT * FROM products WHERE category_id = :categoryId", 
           nativeQuery = true)
    List<Product> findByCategoryNative(@Param("categoryId") Long categoryId);


    // ============================================================
    // 4. NAMED QUERY (searchByMinPrice)
    // ============================================================
    @Query(name = "Product.searchByMinPrice")
    List<Product> searchByMinPrice(@Param("minPrice") double minPrice);


    // ============================================================
    // 5. FETCH JOIN (untuk hilangkan N+1 Problem)
    // ============================================================
    @Query("""
           SELECT p FROM Product p 
           JOIN FETCH p.category 
           WHERE p.id = :id
           """)
    Optional<Product> findByIdWithCategory(@Param("id") Long id);


    // ============================================================
    // 6. ENTITYGRAPH (alternatif Fetch Join)
    // ============================================================
    @EntityGraph(attributePaths = "category")
    Optional<Product> findWithCategoryById(Long id);


    // ============================================================
    // 7. Method Query Otomatis (Spring Data)
    // ============================================================
    List<Product> findByCategory_Id(Long categoryId);

    Page<Product> findByCategory_Id(Long categoryId, Pageable pageable);
}
