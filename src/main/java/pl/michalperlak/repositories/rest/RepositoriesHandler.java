package pl.michalperlak.repositories.rest;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.michalperlak.repositories.domain.RepositoriesFacade;
import pl.michalperlak.repositories.domain.dto.GetRepositoryDTO;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

class RepositoriesHandler {
    private final RepositoriesFacade repositoriesFacade;

    RepositoriesHandler(final RepositoriesFacade repositoriesFacade) {
        this.repositoriesFacade = repositoriesFacade;
    }

    Mono<ServerResponse> getRepository(final ServerRequest serverRequest) {
        String repositoryOwner = serverRequest.pathVariable(REPOSITORY_OWNER_PATH_VARIABLE);
        String repositoryName = serverRequest.pathVariable(REPOSITORY_NAME_PATH_VARIABLE);

        return repositoriesFacade.getRepository(repositoryOwner, repositoryName)
                .flatMap(RepositoriesHandler::createResponse)
                .switchIfEmpty(notFound().build());
    }

    private static Mono<ServerResponse> createResponse(final GetRepositoryDTO repositoryDTO) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(repositoryDTO), GetRepositoryDTO.class);
    }

    private static String pathWithVariables(final String... variables) {
        if (variables.length < 1) {
            return "/";
        }

        return Arrays.stream(variables)
                .collect(Collectors.joining("}/{", "/{", "}"));
    }

    private static final String REPOSITORY_OWNER_PATH_VARIABLE = "repositoryOwner";
    private static final String REPOSITORY_NAME_PATH_VARIABLE = "repositoryName";

    static final String GET_REPOSITORY_PATH =
            "/repositories" + pathWithVariables(REPOSITORY_OWNER_PATH_VARIABLE, REPOSITORY_NAME_PATH_VARIABLE);
}
