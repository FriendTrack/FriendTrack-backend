package com.ciklon.friendtracker.core.entity;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recommendations")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @Column
    private String title;

    @Column
    private String description;
}
