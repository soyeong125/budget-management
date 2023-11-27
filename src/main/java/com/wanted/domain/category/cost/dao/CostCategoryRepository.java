package com.wanted.domain.category.cost.dao;

import com.wanted.domain.category.cost.entity.CostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostCategoryRepository extends JpaRepository<CostCategory, Long> {

}
