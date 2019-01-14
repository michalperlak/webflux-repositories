package pl.michalperlak.repositories.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.michalperlak.repositories.domain.RepositoriesFacade;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
class RepositoriesRouter {
    private static Logger logger = LoggerFactory.getLogger(RepositoriesRouter.class);

    @Bean
    RouterFunction<ServerResponse> route(final RepositoriesHandler repositoriesHandler) {
        return RouterFunctions
                .route(GET(RepositoriesHandler.GET_REPOSITORY_PATH), repositoriesHandler::getRepository)
                .filter(RepositoriesRouter::filterRequests);
    }

    @Bean
    RepositoriesHandler repositoriesHandler(final RepositoriesFacade repositoriesFacade) {
        return new RepositoriesHandler(repositoriesFacade);
    }

    private static Mono<ServerResponse> filterRequests(final ServerRequest serverRequest,
                                                       final HandlerFunction<ServerResponse> handlerFunction) {
        try {
            return handlerFunction.handle(serverRequest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
