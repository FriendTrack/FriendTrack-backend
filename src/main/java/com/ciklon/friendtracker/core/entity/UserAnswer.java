package com.ciklon.friendtracker.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@Table(name = "user_answers")
@NoArgsConstructor
public class UserAnswer {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private QuestionAnswer questionAnswer;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate createdAt;


    public UserAnswer(Question question, QuestionAnswer questionAnswer, Contact contact, User user) {
        this.question = question;
        this.questionAnswer = questionAnswer;
        this.contact = contact;
        this.user = user;
        this.createdAt = LocalDate.now();
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDate.now();
    }
}
