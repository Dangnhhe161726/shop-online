package com.shopping.online.repositories;

import com.shopping.online.models.SizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SizeRepository extends JpaRepository<SizeEntity, Long> {
    boolean existsByName(String name);
}
