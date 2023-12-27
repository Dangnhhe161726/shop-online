package com.shopping.online.repositories;

import com.shopping.online.models.Color;
import com.shopping.online.models.SizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Long> {
    boolean existsByName(String name);
}
