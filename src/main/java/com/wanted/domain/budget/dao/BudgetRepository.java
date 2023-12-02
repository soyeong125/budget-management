package com.wanted.domain.budget.dao;

import com.wanted.domain.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByMemberIdAndCategoryId(Long memberId, Long categoryId);

    @Query("SELECT b "
            + "FROM Budget b "
            + "LEFT JOIN FETCH b.category "
            + "WHERE b.member.id =:memberId")
    List<Budget> findAllByMemberId(@Param("memberId") Long memberId);
}
