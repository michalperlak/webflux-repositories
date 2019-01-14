package pl.michalperlak.repositories.domain;

import pl.michalperlak.repositories.domain.dto.GetRepositoryDTO;
import pl.michalperlak.repositories.internal.ExternalRepository;

final class RepositoriesConverter {
    private RepositoriesConverter() {
    }

    static Repository fromExternal(final ExternalRepository externalRepository) {
        return Repository.builder()
                .fullName(externalRepository.getFullName())
                .description(externalRepository.getDescription())
                .cloneURL(externalRepository.getCloneUrl())
                .stars(externalRepository.getStars())
                .createdAt(externalRepository.getCreatedAt().toLocalDateTime())
                .build();
    }

    static GetRepositoryDTO toGetDTO(final Repository repository) {
        return GetRepositoryDTO.builder()
                .fullName(repository.getFullName())
                .description(repository.getDescription())
                .cloneUrl(repository.getCloneURL())
                .stars(repository.getStars())
                .createdAt(repository.getCreatedAt())
                .build();
    }
}
