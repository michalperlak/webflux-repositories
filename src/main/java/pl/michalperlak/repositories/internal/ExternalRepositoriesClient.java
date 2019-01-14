package pl.michalperlak.repositories.internal;

import reactor.core.publisher.Mono;

public interface ExternalRepositoriesClient {
    Mono<ExternalRepository> getRepository(String owner, String name);
}
