package com.gotrack.branch_service.domain.entity;


import jakarta.persistence.*;
import lombok.*;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="branches")
public class BranchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 100)
    private String name;

    @Column(nullable = false , length = 100)
    private String location;

    @Column(nullable = false , unique = true , length = 11)
    private String phone;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isDeleted = false;

}
