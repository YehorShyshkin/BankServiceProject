package com.bankapp.app.repository;

import com.bankapp.app.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
}
