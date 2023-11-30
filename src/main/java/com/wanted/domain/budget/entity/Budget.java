package com.wanted.domain.budget.entity;

import com.wanted.domain.category.cost.entity.CostCategory;
import com.wanted.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

/**
 * 예산
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id", nullable = false)
    private Long id;

    // 비용 카테고리 (1:1)
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "cost_category_id", nullable = false)
    private CostCategory category;

    // 회원 (N:1)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 개별 예산 금액
    @Column(name = "cost", nullable = false)
    private Integer cost;

    @Builder
    public Budget(Long id, CostCategory category, Member member, Integer cost) {
        this.id = id;
        this.category = category;
        this.member = member;
        this.cost = cost;
    }
}
