package com.ciklon.friendtracker.core.entity;

import com.ciklon.friendtracker.api.dto.enums.EmotionType;
import com.ciklon.friendtracker.core.entity.embedabble.ContactInteractionId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@Entity
@Table(name = "contact_interactions")
@NoArgsConstructor
public class ContactInteraction {
    @EmbeddedId
    private ContactInteractionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("formId")
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("contactId")
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmotionType emotion = EmotionType.LIKE;


    public ContactInteraction(Contact contact, Form form, EmotionType emotion) {
        this.id = new ContactInteractionId(form.getId(), contact.getId());
        this.contact = contact;
        this.form = form;
        this.emotion = emotion;
    }
}