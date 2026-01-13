package ru.otus.homework.security.token;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Token(UUID id, String subject, String gameId, List<String> authorities, Instant createdAt, Instant expiresAt) {
}
