package com.wanted.domain.expenditure.dao;

import com.wanted.domain.expenditure.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {

    List<Expenditure> findAllByMemberId(Long memberId);

}
