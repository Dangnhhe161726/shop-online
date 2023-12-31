package com.shopping.online.services.role;

import com.shopping.online.models.Role;
import com.shopping.online.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;

    @PostConstruct
    @Transactional
    public void initData() {
        createRoles("USER");
        createRoles("SALE");
        createRoles("ADMIN");
        createRoles("SUPER_ADMIN");
        createRoles("SHIPPER");
    }


    private void createRoles(String name) {
        if (!roleRepository.existsByName(name)) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }
}
