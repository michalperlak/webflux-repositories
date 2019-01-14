package pl.michalperlak.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.michalperlak.repositories.domain.dto.GetRepositoryDTO;
import pl.michalperlak.repositories.internal.ExternalRepositoriesClient;
import pl.michalperlak.repositories.internal.ExternalRepository;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RepositoriesEndpointTests {

    @MockBean
    private ExternalRepositoriesClient externalRepositoriesClient;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Should return not found status when repository not found by external client")
    void shouldReturnNotFoundStatusWhenOwnerDoesNotExist() {
        String owner = "testrepositories";
        String repositoryName = "repository1";

        given(externalRepositoriesClient.getRepository(owner, repositoryName))
                .willReturn(Mono.empty());

        webTestClient.method(HttpMethod.GET)
                .uri("/repositories/" + owner + "/" + repositoryName)
                .exchange()
                .expectStatus().isNotFound();
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

        GetRepositoryDTO expected = GetRepositoryDTO.builder()
                .fullName(fullName)
                .description(description)
                .cloneUrl(cloneUrl)
                .stars(stars)
                .createdAt(createdAt.toLocalDateTime())
                .build();

        webTestClient.method(HttpMethod.GET)
                .uri("/repositories/" + owner + "/" + repositoryName)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(GetRepositoryDTO.class)
                .isEqualTo(expected);
    }

}
