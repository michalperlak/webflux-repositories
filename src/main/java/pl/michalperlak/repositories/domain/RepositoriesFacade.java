package pl.michalperlak.repositories.domain;

import lombok.AllArgsConstructor;
import pl.michalperlak.repositories.domain.dto.GetRepositoryDTO;
import pl.michalperlak.repositories.internal.ExternalRepositoriesClient;
import reactor.core.publisher.Mono;


@AllArgsConstructor
public final class RepositoriesFacade {
    private final ExternalRepositoriesClient repositoriesClient;

    public Mono<GetRepositoryDTO> getRepository(final String owner, final String name) {
        return repositoriesClient.getRepository(owner, name)
                .map(RepositoriesConverter::fromExternal)
                .map(RepositoriesConverter::toGetDTO);
    }
}
