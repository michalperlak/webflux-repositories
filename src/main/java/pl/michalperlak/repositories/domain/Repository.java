package pl.michalperlak.repositories.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
class Repository {
    private final String fullName;
    private final String description;
    private final String cloneURL;
    private final int stars;
    private final LocalDateTime createdAt;
}
