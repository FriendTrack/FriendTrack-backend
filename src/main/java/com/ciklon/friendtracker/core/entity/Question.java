package com.ciklon.friendtracker.core.entity;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "questions")
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String question;

    // Тип поля, на который будут начислен рейтинг", например, "COMMUNICATION"
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldType fieldType;
}
