package com.ciklon.friendtracker.core.entity;

import com.ciklon.friendtracker.core.entity.embedabble.ContactInteractionId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@Entity
@Table(name = "contact_interactions")
@NoArgsConstructor
@AllArgsConstructor
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
    private Integer communication = 0;

    @Min(0)
    @Max(10)
    private Integer empathy = 0;

    @Min(0)
    @Max(10)
    private Integer trust = 0;

    @Min(0)
    @Max(10)
    private Integer time = 0;

    @Min(0)
    @Max(10)
    private Integer respect = 0;

    public ContactInteraction(
            Contact contact,
            Form form,
            Integer communication,
            Integer empathy,
            Integer trust,
            Integer time,
            Integer respect
    ) {
        this.id = new ContactInteractionId(form.getId(), contact.getId());
        this.contact = contact;
        this.form = form;
        this.communication = communication;
        this.empathy = empathy;
        this.trust = trust;
        this.time = time;
        this.respect = respect;
    }
}