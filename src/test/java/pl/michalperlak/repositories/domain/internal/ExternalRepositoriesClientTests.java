package pl.michalperlak.repositories.domain.internal;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.michalperlak.repositories.internal.ExternalRepositoriesClient;
import pl.michalperlak.repositories.internal.ExternalRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeoutException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:mockserver.properties")
public class ExternalRepositoriesClientTests {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(MOCK_SERVER_PORT);

    @Autowired
    private ExternalRepositoriesClient repositoriesClient;

    @Test
    public void shouldReturnEmptyResultWhenInternalServerErrorReturned() {
        wireMockRule.stubFor(get(urlPathEqualTo("/repos/owner1/repo"))
                .willReturn(aResponse()
                        .withStatus(SERVER_ERROR)));

        String owner = "owner1";
        String repo = "repo";

        Mono<ExternalRepository> repository = repositoriesClient.getRepository(owner, repo);
        StepVerifier.create(repository)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void shouldReturnErrorWhenConnectionTimeout() {
        wireMockRule.stubFor(get(urlPathEqualTo("/repos/owner2/repo"))
                .willReturn(aResponse()
                        .withStatus(OK)
                        .withFixedDelay(DELAY)));

        String owner = "owner2";
        String repo = "repo";

        Mono<ExternalRepository> repository = repositoriesClient.getRepository(owner, repo);
        StepVerifier.create(repository)
                .expectError(TimeoutException.class)
                .verify();
    }

    @Test
    public void shouldReturnEmptyResultWhenRepositoryNotFound() {
        wireMockRule.stubFor(get(urlPathEqualTo("/repos/owner3/repo"))
                .willReturn(aResponse()
                        .withStatus(NOT_FOUND)));

        String owner = "owner3";
        String repo = "repo";

        Mono<ExternalRepository> repository = repositoriesClient.getRepository(owner, repo);
        StepVerifier.create(repository)
                .expectNextCount(0)
                .verifyComplete();
    }

    private static final int MOCK_SERVER_PORT = 9099;
    private static final int SERVER_ERROR = 500;
    private static final int NOT_FOUND = 404;
    private static final int OK = 200;
    private static final int DELAY = 20000;
}
