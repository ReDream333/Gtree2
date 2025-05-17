package ru.kpfu.itis.kononenko.gtree2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.kononenko.gtree2.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}