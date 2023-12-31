package com.shopping.online.repositories;

import com.shopping.online.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Boolean existsByName(String name);
}
