package com.ciklon.friendtracker.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "question_answers")
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswer {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "question_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private boolean isPositive;


    public QuestionAnswer(String answer, Question question, boolean isPositive) {
        this.answer = answer;
        this.question = question;
        this.isPositive = isPositive;
        this.id = UUID.randomUUID();
    }
}
