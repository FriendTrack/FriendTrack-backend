package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.core.entity.ContactInteraction;
import com.ciklon.friendtracker.core.entity.embedabble.ContactInteractionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactInteractionRepository extends JpaRepository<ContactInteraction, ContactInteractionId> {
}
