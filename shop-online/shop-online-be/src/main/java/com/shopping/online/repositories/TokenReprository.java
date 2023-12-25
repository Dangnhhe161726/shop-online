package com.shopping.online.repositories;

import com.shopping.online.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenReprository extends JpaRepository<Token, Long> {
}
