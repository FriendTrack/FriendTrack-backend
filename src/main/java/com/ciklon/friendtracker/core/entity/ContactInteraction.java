package com.ciklon.friendtracker.core.entity;

import com.ciklon.friendtracker.api.dto.enums.InteractionMark;
import com.ciklon.friendtracker.api.dto.enums.InteractionType;
import com.ciklon.friendtracker.core.entity.embedabble.ContactInteractionId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Min(0)
    @Max(10)
    private Integer happiness = 0;

    @Min(0)
    @Max(10)
    private Integer sadness = 0;

    @Min(0)
    @Max(10)
    private Integer fear = 0;

    @Min(0)
    @Max(10)
    private Integer disgust = 0;

    @Min(0)
    @Max(10)
    private Integer anger = 0;

    @Min(0)
    @Max(10)
    private Integer surprise = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InteractionMark interactionMark = InteractionMark.LIKE;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InteractionType interactionType = InteractionType.MEETING;


    public ContactInteraction(
            Contact contact,
            Form form,
            Integer happiness,
            Integer sadness,
            Integer fear,
            Integer disgust,
            Integer anger,
            Integer surprise,
            InteractionMark interactionMark,
            InteractionType interactionType
    ) {
        this.id = new ContactInteractionId(form.getId(), contact.getId());
        this.contact = contact;
        this.form = form;
        this.happiness = happiness;
        this.sadness = sadness;
        this.fear = fear;
        this.disgust = disgust;
        this.anger = anger;
        this.surprise = surprise;
        this.interactionMark = interactionMark;
        this.interactionType = interactionType;
    }
}