package ru.kpfu.itis.kononenko.gtree2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kpfu.itis.kononenko.gtree2.dto.response.RelationResponse;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node, Long> {
    List<Node> findByTreeId(Long treeId);

    @Query("select concat(n.firstName,' ',n.lastName) from Node n where n.id = :id")
    String findFullName(@Param("id") Long id);

    @Query("""
    SELECT new ru.kpfu.itis.kononenko.gtree2.dto.response.RelationResponse(c.id, p.id)
      FROM Node p
      JOIN p.child c
     WHERE p.tree.id = :treeId
  """)
    List<RelationResponse> findRelationsByTreeId(@Param("treeId") Long treeId);
}