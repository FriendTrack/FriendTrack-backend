package com.ciklon.friendtracker.core.entity.embedabble;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInteractionId implements Serializable {
    @Column(name = "form_id")
    private UUID formId;

    @Column(name = "contact_id")
    private UUID contactId;
}
