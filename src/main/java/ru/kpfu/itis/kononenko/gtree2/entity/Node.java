package ru.kpfu.itis.kononenko.gtree2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "nodes")
public class Node {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "tree_id")
        private Tree tree;


        @Column(name = "first_name")
        private String firstName;

        @Column(name = "last_name")
        private String lastName;

        @Column(name = "genre")
        private char gender;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Column(name = "birth_date")
        private LocalDate birthDate;

        @Column(name = "death_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate  deathDate;

        @Column(name = "comment")
        private String comment;

        @Column(name = "photo_url")
        private String photoUrl;

        @ManyToMany
        @JoinTable(
                name = "node_relation",
                joinColumns = @JoinColumn(name = "child_id"),
                inverseJoinColumns = @JoinColumn(name = "parent_id")
        )
        private Set<Node> parents = new HashSet<>();
}