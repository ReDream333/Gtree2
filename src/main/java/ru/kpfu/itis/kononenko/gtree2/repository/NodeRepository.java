package ru.kpfu.itis.kononenko.gtree2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kpfu.itis.kononenko.gtree2.dto.response.RelationResponse;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node, Long> {
    List<Node> findByTreeId(Long treeId);

    @Query("""
    SELECT new ru.kpfu.itis.kononenko.gtree2.dto.response.RelationResponse(c.id, p.id)
      FROM Node p
      JOIN p.child c
     WHERE p.tree.id = :treeId
  """)
    List<RelationResponse> findRelationsByTreeId(@Param("treeId") Long treeId);


    @Query("SELECT n.zodiacSign, COUNT(n) FROM Node n WHERE n.tree.id = :treeId and (n.zodiacSign is not null) GROUP BY n.zodiacSign")

    List<Object[]> countZodiacSignsByTreeId(@Param("treeId") Long treeId);

    @Query("SELECT n FROM Node n WHERE n.birthDate IS NOT NULL AND MONTH(n.birthDate) = :month AND DAY(n.birthDate) = :day")
    List<Node> findByBirthday(@Param("month") int month, @Param("day") int day);
}