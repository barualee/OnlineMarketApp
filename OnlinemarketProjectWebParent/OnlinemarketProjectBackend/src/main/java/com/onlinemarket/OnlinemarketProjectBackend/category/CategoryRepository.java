package com.onlinemarket.OnlinemarketProjectBackend.category;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer>, PagingAndSortingRepository<Category, Integer> {
    
    @Query("SELECT c FROM Category c WHERE c.parent.id IS NULL")
    public Page<Category> findRootCategories(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.parent.id IS NULL")
    public List<Category> findRootCategories(Sort sort);

    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
    public Page<Category> search(String keyword, Pageable pageable);

    public Long countById(Integer id);
    
    //can be used for unique columns
    public Category findByName(String name);

    public Category findByAlias(String alias);

    @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);
}
