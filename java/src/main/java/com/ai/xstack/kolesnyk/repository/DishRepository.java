package com.ai.xstack.kolesnyk.repository;

import com.ai.xstack.kolesnyk.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<DishEntity, Long> {
}
