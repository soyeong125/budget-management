package com.wanted.domain.category.cost.dao;

import com.wanted.domain.category.cost.constants.CategoryName;
import com.wanted.domain.category.cost.entity.CostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CostCategoryRepository extends JpaRepository<CostCategory, Long> {
    Optional<CostCategory> findByName(String name);
}
