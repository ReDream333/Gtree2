package ru.kpfu.itis.kononenko.gtree2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.kononenko.gtree2.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByEmailVerifiedTrue();
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    void deleteByUsername(String username);
}
