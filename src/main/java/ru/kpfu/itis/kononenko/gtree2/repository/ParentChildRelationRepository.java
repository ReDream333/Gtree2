package ru.kpfu.itis.kononenko.gtree2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kpfu.itis.kononenko.gtree2.entity.ParentChildRelation;

import java.util.List;

public interface ParentChildRelationRepository extends JpaRepository<ParentChildRelation, Long> {
    @Query("select r from ParentChildRelation r where r.parent.tree.id = :treeId or r.child.tree.id = :treeId")
    List<ParentChildRelation> findByTree(@Param("treeId") Long treeId);
}