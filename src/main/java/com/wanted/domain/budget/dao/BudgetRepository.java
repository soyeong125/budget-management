package com.wanted.domain.budget.dao;

import com.wanted.domain.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByMemberIdAndCategoryId(Long memberId, Long categoryId);
}
