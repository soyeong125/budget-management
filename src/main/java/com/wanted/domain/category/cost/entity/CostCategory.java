package com.wanted.domain.category.cost.entity;

import com.wanted.domain.category.cost.constants.CategoryName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비용 카테고리
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "cost_category")
public class CostCategory {

    // 카테고리 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cost_category_id", nullable = false)
    private Long id;

    // 카테고리 이름
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private CategoryName name;

    @Builder
    private CostCategory(Long id, CategoryName name) {
        this.id = id;
        this.name = name;
    }
}
