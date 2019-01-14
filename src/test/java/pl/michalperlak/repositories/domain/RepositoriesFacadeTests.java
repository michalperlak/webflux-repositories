package pl.michalperlak.repositories.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.michalperlak.repositories.domain.dto.GetRepositoryDTO;
import pl.michalperlak.repositories.internal.ExternalRepositoriesClient;
import pl.michalperlak.repositories.internal.ExternalRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.ZonedDateTime;

import static org.mockito.BDDMockito.given;

class RepositoriesFacadeTests {

    @Mock
    private ExternalRepositoriesClient externalRepositoriesClient;

    private RepositoriesFacade repositoriesFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        repositoriesFacade = new RepositoriesFacade(externalRepositoriesClient);
    }

    @Test
    @DisplayName("Should return empty result when repository with name has not been found")
    void shouldReturnEmptyMonoWhenRepositoryWithNameNotFound() {
        String owner = "testrepositories";
        String repositoryName = "notExistingRepository";

        given(externalRepositoriesClient.getRepository(owner, repositoryName))
                .willReturn(Mono.empty());

        Mono<GetRepositoryDTO> repository = repositoriesFacade.getRepository(owner, repositoryName);

        StepVerifier.create(repository)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return empty result when repository owner has not been found")
    void shouldReturnEmptyResultWhenRepositoryOwnerNotFound() {
        String owner = "aBzASfsaDHFHFCVBNVDESQWrqewgdbfsadasvbvprqewdfslxcvxcv";
        String repositoryName = "repository1";

        given(externalRepositoriesClient.getRepository(owner, repositoryName))
                .willReturn(Mono.empty());

        Mono<GetRepositoryDTO> repository = repositoriesFacade.getRepository(owner, repositoryName);

        StepVerifier.create(repository)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return result with proper repository data when owner name and repository name are correct")
    void shouldReturnResultWithProperDataWhenOwnerNameAndRepoNameAreCorrect() {
        String owner = "testrepositories";
        String repositoryName = "repository1";

        String fullName = owner + "/" + repositoryName;
        String description = "This is first repository";
        String cloneUrl = "https://github.com/testrepositories/repository1.git";
        int stars = 1;
        ZonedDateTime createdAt = ZonedDateTime.parse("2018-05-26T16:30:51Z");

        ExternalRepository externalRepository = ExternalRepository.builder()
                .fullName(fullName)
                .description(description)
                .cloneUrl(cloneUrl)
                .stars(stars)
                .createdAt(createdAt)
                .build();

        given(externalRepositoriesClient.getRepository(owner, repositoryName))
                .willReturn(Mono.just(externalRepository));

        Mono<GetRepositoryDTO> repository = repositoriesFacade.getRepository(owner, repositoryName);

        GetRepositoryDTO expected = GetRepositoryDTO.builder()
                .fullName(fullName)
                .description(description)
                .cloneUrl(cloneUrl)
                .stars(stars)
                .createdAt(createdAt.toLocalDateTime())
                .build();

        StepVerifier.create(repository)
                .expectNext(expected)
                .verifyComplete();
    }
}
