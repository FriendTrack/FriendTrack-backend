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

    public QuestionAnswer(String answer, Question question) {
        this.answer = answer;
        this.question = question;
        this.id = UUID.randomUUID();
    }
}
